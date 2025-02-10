package project2.service;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import project2.dto.UserProfileUpdateRequest;
import project2.dto.UserRegistrationDto;
import project2.dto.UserResponse;
import project2.entity.Users;
import project2.exception.UserNotFoundException;
import project2.exception.UsernameAlreadyExistsException;
import project2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

    public Users registerUserAccount(UserRegistrationDto registrationDto) { // 회원 정보 저장
        Users user = new Users();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        // 프로필 이미지 url 정보 저장용 코드 작성 필요
        return userRepository.save(user);
    }
    
    public Users getUserById(Long uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
    }

    public UserResponse updateUserProfile(Long uid, UserProfileUpdateRequest request, MultipartFile image) {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
        
        // username 중복 체크 (현재 user가 아닌 다른 유저가 같은 username을 가지고 있는지 확인)
        Optional.ofNullable(request.getUsername())
                .filter(username -> !username.equals(user.getUsername())) // 기존 username과 다른 경우만 검사
                .flatMap(username -> userRepository.existsByUsername(username) ? 
                         Optional.of(username) : Optional.empty())
                .ifPresent(username -> {
                    throw new UsernameAlreadyExistsException("이미 사용 중인 사용자명입니다.");
                });

        String imageUrl = user.getProfileImageUrl();

        // 새 이미지가 업로드된 경우 기존 이미지 삭제 후 업로드
        if (image != null && !image.isEmpty()) {
            if (imageUrl != null) {
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                amazonS3.deleteObject(bucketName, fileName);
            }

            String fileName = "profile/" + UUID.randomUUID() + "-" + image.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());

            try {
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image to S3", e);
            }
            imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
        }

        // 기존 객체를 변경하지 않고, 새로운 객체를 만들어 저장
        Users updatedUser = user.updateProfile(request.getUsername(), request.getBio(), imageUrl);
        userRepository.save(updatedUser);

        return UserResponse.builder()
                .uid(updatedUser.getUid())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .bio(updatedUser.getBio())
                .profileImageUrl(updatedUser.getProfileImageUrl())
                .build();
    }
}
