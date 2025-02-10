package project2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.entity.Posts;
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

}
