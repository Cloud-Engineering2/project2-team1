package project2.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import project2.dto.PostCreateRequest;
import project2.dto.PostResponse;
import project2.entity.Posts;
import project2.entity.Users;
import project2.exception.ImageUploadException;
import project2.exception.PostNotFoundException;
import project2.exception.UserNotFoundException;
import project2.repository.PostRepository;
import project2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PostService {
	private final PostRepository postRepository;
	private final AmazonS3 amazonS3;
	private final UserRepository userRepository;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	
	// 1️ 전체 게시물 조회
    public List<Posts> getAllPosts() {
        return postRepository.findAll();
    }
    
    // 2️ 특정 사용자 게시물 조회
    public List<Posts> getPostsByUser(Long uid) {
    	Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
    	List<Posts> posts = postRepository.findByUserUid(uid);
    	return posts;
    }
    
    // 3️ 특정 게시물 상세 조회
    public Posts getPostById(Long pid) {
        return postRepository.findById(pid)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid)); // 예외 발생
    }

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
			    throw new ImageUploadException("Failed to upload new image to S3", e);
			} catch (AmazonClientException e) {
			    throw new ImageUploadException("Error communicating with S3", e);
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

	public PostResponse updatePost(Long pid, PostCreateRequest request, MultipartFile image) {
		Posts post = postRepository.findById(pid)
				.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid));
		
		String imageUrl = post.getImageUrl();
		
		// 새 이미지가 업로드된 경우 기존 이미지 삭제 후 업로드
		if (image != null && !image.isEmpty()) {
			if (imageUrl != null) {
				String fileName = imageUrl.substring(imageUrl.indexOf("posts/"));
				amazonS3.deleteObject(bucketName, fileName);
			}
			
			String fileName = "posts/" + UUID.randomUUID() + "-" + image.getOriginalFilename();
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
		
		post.updatePost(
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

	public void deletePost(Long pid) {
		Posts post = postRepository.findById(pid)
				.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid));
		
		String imageUrl = post.getImageUrl();
		if (imageUrl != null) {
			String fileName = imageUrl.substring(imageUrl.indexOf("posts/"));
			amazonS3.deleteObject(bucketName, fileName);
		}
		postRepository.delete(post);
	}
}
