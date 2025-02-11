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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project2.dto.CommentRequest;
import project2.dto.CommentResponse;
import project2.service.CommentService;

@RestController
@RequestMapping("/api/posts/{pid}")
@RequiredArgsConstructor
public class CommentController {
	
    private final CommentService commentService;

    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(@PathVariable Long pid) {
    	List<CommentResponse> comments = commentService.getCommentsByPostId(pid);
    	return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> createComment(
    		@PathVariable Long pid,
    		@RequestBody CommentRequest request) {
        CommentResponse createdComment = commentService.createComment(pid, request);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping("/comments/{cid}")
    public ResponseEntity<CommentResponse> updateComment(
    		@PathVariable Long pid,
    		@PathVariable Long cid,
    		@RequestBody CommentRequest request) {
        CommentResponse updatedComment = commentService.updateComment(pid, cid, request);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{cid}")
    public ResponseEntity<?> deleteComment(
    		@PathVariable Long pid,
    		@PathVariable Long cid) {
        commentService.deleteComment(pid, cid);
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "Successfully delete comment.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
