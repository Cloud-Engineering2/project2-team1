package project2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project2.controller.dto.CommentRequest;
import project2.entity.Comments;
import project2.service.CommentService;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/post")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{pid}/comments")
    public List<Comments> getCommentsByPostId(@PathVariable Long pid) {
        return commentService.getCommentsByPostId(pid);
    }

    @PostMapping("/{pid}/comments")
    public ResponseEntity<Comments> addComment(@PathVariable Long pid, @RequestBody CommentRequest request) {
        Comments comment = commentService.addComment(pid, request.getUserId(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PutMapping("/{pid}/comments/{cid}")
    public ResponseEntity<Comments> updateComment(@PathVariable Long cid, @RequestBody CommentRequest request) {
        Comments comment = commentService.updateComment(cid, request.getContent());
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{pid}/comments/{cid}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long cid) {
        commentService.deleteComment(cid);
        return ResponseEntity.noContent().build();
    }
}
