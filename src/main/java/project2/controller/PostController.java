package project2.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project2.dto.PostRequest;
import project2.dto.PostResponse;
import project2.entity.Posts;
import project2.service.PostService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	
	
	// 전체 게시물 조회 (GET /api/posts)
    @GetMapping("/posts")
    public ResponseEntity<List<Posts>> getAllPosts() {
        List<Posts> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 사용자의 게시물 조회 (GET /api/users/{uid}/posts)
    @GetMapping("/users/{uid}/posts")
    public ResponseEntity<List<Posts>> getPostsByUser(@PathVariable("uid") Long uid) {
        List<Posts> posts = postService.getPostsByUser(uid);
        return ResponseEntity.ok(posts);
    }

    // 특정 게시물 상세 조회 (GET /api/posts/{pid})
    @GetMapping("/posts/{pid}")
    public ResponseEntity<Posts> getPostById(@PathVariable("pid") Long pid) {
        Posts post = postService.getPostById(pid); // 예외 발생 가능
        return ResponseEntity.ok(post);
    }
    
    // 게시물 생성
	@PostMapping(value = "/posts", consumes = {"multipart/form-data"})
	public ResponseEntity<PostResponse> createPost(
			@RequestPart("post") PostRequest postRequest,
			@RequestPart(value = "image", required = false) MultipartFile[] images) {
		PostResponse createdPost = postService.createPost(postRequest, images);
		return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
	}
	
	// 게시물 수정
	@PutMapping(value = "/posts/{pid}", consumes = {"multipart/form-data"})
	public ResponseEntity<PostResponse> updatePost(
			@PathVariable Long pid,
			@RequestPart("post") PostRequest postRequest,
			@RequestPart(value = "image", required = false) MultipartFile[] images) {
		PostResponse updatedPost = postService.updatePost(pid, postRequest, images);
		return new ResponseEntity<>(updatedPost, HttpStatus.OK);
	}
	
	// 게시물 삭제
	@DeleteMapping(value = "/posts/{pid}")
	public ResponseEntity<?> deletePost(@PathVariable Long pid) {
		postService.deletePost(pid);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Successfully delete post.");
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
