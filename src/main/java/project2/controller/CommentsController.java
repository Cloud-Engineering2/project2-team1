//package project2.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//import project2.entity.Comments;
//import project2.service.CommentService;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/comments")
//public class CommentController {
//    
//	@Autowired
//    private CommentService commentService;
//
//    @GetMapping("/post/{postId}")
//    public List<Comments> getCommentsByPostId(@PathVariable Long postId) {
//        return commentService.getCommentsByPostId(postId);
//    }
//
//    @PostMapping("/")
//    public Comments addComment(@RequestParam Long postId, @RequestParam Long userId, @RequestParam String content) {
//        return commentService.addComment(postId, userId, content);
//    }
//}

//24.02.08 경민수정ver

package project2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project2.controller.dto.CommentRequest;
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
    public ResponseEntity<Comments> addComment(@RequestBody CommentRequest commentRequest) {
        Comments comment = commentService.addComment(
                commentRequest.getPostId(),
                commentRequest.getUserId(),
                commentRequest.getContent()
        );
        return ResponseEntity.ok(comment);
    }
}


//24.02.09 경민수정ver
