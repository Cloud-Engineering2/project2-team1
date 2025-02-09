package project2.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import project2.dto.PostCreateRequest;
import project2.dto.PostResponse;
import project2.entity.Posts;
import project2.entity.Users;
import project2.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final AmazonS3 amazonS3;
	
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public PostResponse createPost(PostCreateRequest request, MultipartFile image) {
		String imageUrl = null;
		
		// 이미지 파일이 존재하면 S3에 업로드
		if (image != null && !image.isEmpty()) {
			// posts-UUID-원본파일명 형식으로 파일 저장 
			String fileName = "posts/" + UUID.randomUUID() + "-" + image.getOriginalFilename();
			ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(image.getSize());
			try {
				amazonS3.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));
			} catch (IOException e) {
				throw new RuntimeException("Failed to upload image to S3", e);
			}
			imageUrl = amazonS3.getUrl(bucketName, fileName).toString();
		}
		
		// Dummy User
		Users user = new Users(
				1L,
				"dummy",
				"dummy",
				"dummy@email.com",
				null,
				"dummy"
		);
		
		Posts post = new Posts(
				null,
				user,
				request.getContent(),
				request.getMealDate(),
                request.getMealType(),
                request.getCalories(),
                imageUrl
		);
		
		Posts savedPost = postRepository.save(post);
		
		return PostResponse.builder()
                .pid(savedPost.getPid())
                .uid(savedPost.getUser().getUid())
                .content(savedPost.getContent())
                .mealDate(savedPost.getMealDate())
                .mealType(savedPost.getMealType())
                .calories(savedPost.getCalories())
                .imageUrl(savedPost.getImageUrl())
                .build();
	}
}
