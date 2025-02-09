package project2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import project2.dto.PostCreateRequest;
import project2.dto.PostResponse;
import project2.service.PostService;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	
	@PostMapping(consumes = {"multipart/form-data"})
	public ResponseEntity<PostResponse> createPost(
			@RequestPart("post") PostCreateRequest postRequest,
			@RequestPart(value = "image", required = false) MultipartFile image) {
		PostResponse createdPost = postService.createPost(postRequest, image);
		return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
	}
}
