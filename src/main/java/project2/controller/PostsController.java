package project2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.entity.Posts;
import project2.exception.PostNotFoundException;
import project2.service.PostsService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;

    // 전체 게시물 조회 (GET /api/posts)
    @GetMapping
    public ResponseEntity<List<Posts>> getAllPosts() {
        List<Posts> posts = postsService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 특정 사용자의 게시물 조회 (GET /api/posts/user/{uid})
    @GetMapping("/users/{uid}")
    public ResponseEntity<List<Posts>> getPostsByUser(@PathVariable("uid") Long uid) {
        List<Posts> posts = postsService.getPostsByUser(uid);
        return ResponseEntity.ok(posts);
    }

    // 특정 게시물 상세 조회 (GET /api/posts/{pid})
    @GetMapping("/{pid}")
    public ResponseEntity<Posts> getPostById(@PathVariable("pid") Long pid) {
        Posts post = postsService.getPostById(pid); // 예외 발생 가능
        return ResponseEntity.ok(post);
    }

    // 예외 발생 시 404 응답 반환
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<String> handlePostNotFound(PostNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
