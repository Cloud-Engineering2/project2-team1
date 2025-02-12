package project2.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import project2.config.PrincipalDetails;
import project2.dto.PostRequest;
import project2.dto.PostResponse;
import project2.entity.Posts;
import project2.entity.Users;
import project2.exception.ImageUploadException;
import project2.exception.PostNotFoundException;
import project2.exception.UnauthorizedException;
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
	
	// 전체 게시물 조회
    public List<Posts> getAllPosts() {
        return postRepository.findAll();
    }
    
    // 특정 사용자 게시물 조회
    public List<Posts> getPostsByUser(Long uid) {
    	userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + uid));
    	List<Posts> posts = postRepository.findByUserUid(uid);
    	return posts;
    }
    
    //️ 특정 게시물 상세 조회
    public Posts getPostById(Long pid) {
        return postRepository.findById(pid)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid)); // 예외 발생
    }

    // 게시물 생성
	public PostResponse createPost(PostRequest request, MultipartFile[] images) {
		List<String> imageUrls = new ArrayList<>();
		
		// 이미지 파일이 존재하면 S3에 업로드
		if (images != null && images.length > 0) {
			for (MultipartFile image : images) {
				if (image != null && !image.isEmpty()) {
					// posts/UUID-원본파일명 형식으로 파일 저장 
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
					String url = amazonS3.getUrl(bucketName, fileName).toString();
					imageUrls.add(url);
				}
			}
			
		}
		
    	Users user = getLoginUser();
		
		Posts post = new Posts(
				null,
				user,
				request.getContent(),
				request.getMealDate(),
                request.getMealType(),
                request.getCalories(),
                imageUrls,
                new ArrayList<>()
		);
		
		Posts savedPost = postRepository.save(post);
		
		return PostResponse.builder()
                .pid(savedPost.getPid())
                .uid(savedPost.getUser().getUid())
                .content(savedPost.getContent())
                .mealDate(savedPost.getMealDate())
                .mealType(savedPost.getMealType())
                .calories(savedPost.getCalories())
                .imageUrlList(savedPost.getImageUrls())
                .build();
	}

	// 게시물 수정
	public PostResponse updatePost(Long pid, PostRequest request, MultipartFile[] images) {
		Posts post = postRepository.findById(pid)
				.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid));
		
    	Users currentUser = getLoginUser();
    	
    	if (!post.getUser().getUid().equals(currentUser.getUid())) {
    		throw new UnauthorizedException("No permission to edit the post");
    	}
    	
    	Map<String, String> existingImageMap = new HashMap<>();
    	for (String imageUrl : post.getImageUrls()) {
    		String fileName = imageUrl.substring(imageUrl.lastIndexOf("-") + 1);
    		existingImageMap.put(fileName, imageUrl);
    	}
        List<String> updatedImageUrls = new ArrayList<>();
        List<String> imagesToDelete = new ArrayList<>();
		
    	// 새로운 요청에 따른 이미지 파일 수정
    	if (images != null && images.length > 0) {
    		for (MultipartFile image : images) {
    			if (image.isEmpty()) continue;
    			
				String fileName = image.getOriginalFilename();
				System.out.println("fileName: " + fileName);
				System.out.println("existing: " + existingImageMap);
				if (existingImageMap.containsKey(fileName)) {
					// 기존 이미지 재사용
					updatedImageUrls.add(existingImageMap.get(fileName));
					existingImageMap.remove(fileName);
				} else {
					// 새로운 이미지 업로드
					String s3FileName = "posts/" + UUID.randomUUID() + "-" + fileName;
					ObjectMetadata metadata = new ObjectMetadata();
		            metadata.setContentLength(image.getSize());
					try {
						amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, image.getInputStream(), metadata)
								.withCannedAcl(CannedAccessControlList.PublicRead));
					} catch (IOException e) {
					    throw new ImageUploadException("Failed to upload new image to S3", e);
					} catch (AmazonClientException e) {
					    throw new ImageUploadException("Error communicating with S3", e);
					}
					updatedImageUrls.add(amazonS3.getUrl(bucketName, s3FileName).toString());
				}
    		}
    	}
    	
    	// 재사용되지 않는 이미지
		imagesToDelete.addAll(existingImageMap.values());
		
    	// 게시물 업데이트
		post.updatePost(
				request.getContent(), 
				request.getMealDate(), 
				request.getMealType(), 
				request.getCalories(), 
				updatedImageUrls
		);
		
		Posts savedPost = postRepository.save(post);
		
		if (!imagesToDelete.isEmpty()) {
			imagesToDelete.forEach(url -> {
				String fileName = url.substring(url.indexOf("posts/"));
				amazonS3.deleteObject(bucketName, fileName);
			});
		}
		
		return PostResponse.builder()
                .pid(savedPost.getPid())
                .uid(savedPost.getUser().getUid())
                .content(savedPost.getContent())
                .mealDate(savedPost.getMealDate())
                .mealType(savedPost.getMealType())
                .calories(savedPost.getCalories())
                .imageUrlList(savedPost.getImageUrls())
                .build();
	}

	// 게시물 삭제
	public void deletePost(Long pid) {
		Posts post = postRepository.findById(pid)
				.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid));
		
    	Users currentUser = getLoginUser();
    	
    	if (!post.getUser().getUid().equals(currentUser.getUid())) {
    		throw new UnauthorizedException("No permission to delete the post");
    	}
		
		List<String> imageUrls = post.getImageUrls();
		if (!imageUrls.isEmpty()) {
			imageUrls.forEach(url -> {
				String fileName = url.substring(url.indexOf("posts/"));
				amazonS3.deleteObject(bucketName, fileName);
			});
		}
		postRepository.delete(post);
	}
	
	private Users getLoginUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !(authentication.getPrincipal() instanceof PrincipalDetails)) {
			throw new UnauthorizedException("Unauthenticated User");
		}
    	PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
    	return userDetails.getUser();
	}
}
