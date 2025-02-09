package project2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project2.entity.Comments;
import project2.entity.Posts;
import project2.entity.Users;
import project2.repository.CommentRepository;
import project2.repository.PostRepository;

import java.util.Optional;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    public List<Comments> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostPid(postId);
    }

    public Comments addComment(Long postId, Long userId, String content) {
        Optional<Posts> postOptional = postRepository.findById(postId);
        if (!postOptional.isPresent()) {
            throw new IllegalArgumentException("The post does not exist");
        }

        Posts post = postOptional.get();
        Users user = new Users();
        user.setUid(userId);

        Comments comment = new Comments();
        comment.setPost(post);
        comment.setUser(user);
        comment.setComment(content);
        return commentRepository.save(comment);
    }
}


//24.02.08 경민수정ver