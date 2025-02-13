package project2.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import project2.dto.UserProfileUpdateRequest;
import project2.dto.UserRegistrationDto;
import project2.dto.UserResponse;
import project2.entity.Users;
import project2.exception.ImageUploadException;
import project2.exception.UserNotFoundException;
import project2.exception.UserRegistrationException;
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
    @Value("${cloud.aws.s3.default.profile}")
    private String defaultProfileImage;
	
    public Users registerUserAccount(UserRegistrationDto registrationDto, MultipartFile image) { // 회원 정보 저장
        Users user = new Users();
        String imageUrl = null;

        try { // 중복 사용자/메일 주소 확인
            if (userRepository.existsByUsername(registrationDto.getUsername())) {
                throw new UserRegistrationException("Username " + registrationDto.getUsername() + " is already taken!");
            }
            if (userRepository.existsByEmail(registrationDto.getEmail())) {
                throw new UserRegistrationException("Email " + registrationDto.getEmail() + " is already in use!");
            }
        } catch (UserRegistrationException e) {
            throw new UserRegistrationException(e.getMessage());
        }

        // 이미지 파일이 존재하면 S3에 업로드
        if (image != null && !image.isEmpty()) {
        	imageUrl = uploadImageToS3(image);
        } 
        else {	// 이미지 파일이 없으면 기본 이미지 설정
        	imageUrl = defaultProfileImage; 
        }

        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setProfileImageUrl(imageUrl);
        user.setBio(registrationDto.getBio());
        return userRepository.save(user);
    }
    
    public UserResponse getUserById(Long uid) {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
        return UserResponse.toDto(user);
    }

    public UserResponse updateUserProfile(Long uid, UserProfileUpdateRequest request, MultipartFile image) {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
        
        // 요청된 username과 현재 username 비교
        String reqUsername = request.getUsername();
        String currentUsername = user.getUsername();

        // 요청된 username이 새로운 username이면서 이미 존재하는 username이라면 예외 발생
        if (reqUsername != null && !reqUsername.equals(currentUsername) && userRepository.existsByUsername(reqUsername)) {
            throw new UsernameAlreadyExistsException("이미 사용 중인 사용자명입니다.");
        }

        String imageUrl = user.getProfileImageUrl();

        // 새 이미지가 업로드된 경우 기존 이미지 삭제 후 업로드
        if (image != null && !image.isEmpty()) {
            if (imageUrl != null && imageUrl.equals(defaultProfileImage)) {
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                amazonS3.deleteObject(bucketName, fileName);
            }
            imageUrl = uploadImageToS3(image);
        }

        user.updateProfile(reqUsername, request.getBio(), imageUrl);
        Users updatedUser = userRepository.save(user);

        return UserResponse.toDto(updatedUser);
    }

    public void deleteUser(Long uid) {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));

        userRepository.delete(user);
    }
    
    private String uploadImageToS3(MultipartFile image) {
    	// profile/UUID-원본파일명 형식으로 파일 저장
    	String fileName = "profile/" + UUID.randomUUID() + "-" + image.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(image.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ImageUploadException("Failed to upload image to S3", e);
        } catch (AmazonClientException e) {
            throw new ImageUploadException("Error communicating with S3", e);
        }
        return amazonS3.getUrl(bucketName, fileName).toString();
    }
}
