package project2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project2.entity.Comments;
import project2.service.CommentService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/comments")
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/post/{postId}")
    public List<Comments> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping("/")
    public ResponseEntity<Comments> addComment(@RequestParam Long postId, @RequestParam Long userId, @RequestParam String content) {
        Comments comment = commentService.addComment(postId, userId, content);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{cid}")
    public ResponseEntity<Comments> updateComment(@PathVariable Long cid, @RequestParam String content) {
        Comments comment = commentService.updateComment(cid, content);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{cid}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long cid) {
        commentService.deleteComment(cid);
        return ResponseEntity.noContent().build();
    }
}

