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
import project2.dto.UserRegistrationDto;
import project2.entity.Users;
import project2.exception.ImageUploadException;
import project2.exception.UserNotFoundException;
import project2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
	private String bucketName;

    public Users registerUserAccount(UserRegistrationDto registrationDto, MultipartFile image) { // 회원 정보 저장
        Users user = new Users();
        String imageUrl = null;

        // 이미지 파일이 존재하면 S3에 업로드
        if (image != null && !image.isEmpty()) {
            // users-UUID-원본파일명 형식으로 파일 저장
            String fileName = "users/" + UUID.randomUUID() + "-" + image.getOriginalFilename();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
            try {
                amazonS3.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ImageUploadException("Failed to upload new image to S3", e);
            } catch (AmazonClientException e) {
                throw new ImageUploadException("Error communicating with S3", e);
            }
            imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
        }

        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        user.setProfileImageUrl(imageUrl);
        user.setBio(registrationDto.getBio());
        return userRepository.save(user);
    }
    
    public Users getUserById(Long uid) {
        return userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
    }


}
