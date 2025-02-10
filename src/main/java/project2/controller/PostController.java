package project2.controller;

import java.util.List;

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
import project2.dto.PostCreateRequest;
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

    // 특정 사용자의 게시물 조회 (GET /api/posts/users/{uid})
    @GetMapping("/posts/users/{uid}")
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
    
	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<PostResponse> createPost(
			@RequestPart("post") PostCreateRequest postRequest,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		PostResponse createdPost = postService.createPost(postRequest, image);
		return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{pid}", consumes = {"multipart/form-data"})
	public ResponseEntity<PostResponse> updatePost(
			@PathVariable Long pid,
			@RequestPart("post") PostCreateRequest postRequest,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		PostResponse updatedPost = postService.updatePost(pid, postRequest, image);
		return new ResponseEntity<>(updatedPost, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{pid}")
	public ResponseEntity<Void> deletePost(@PathVariable Long pid) {
		postService.deletePost(pid);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
