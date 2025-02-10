package project2.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project2.config.PrincipalDetails;
import project2.dto.CommentRequest;
import project2.dto.CommentResponse;
import project2.entity.Comments;
import project2.entity.Posts;
import project2.entity.Users;
import project2.exception.CommentNotBelongToPostException;
import project2.exception.CommentNotFoundException;
import project2.exception.PostNotFoundException;
import project2.exception.UnauthorizedException;
import project2.repository.CommentRepository;
import project2.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long pid) {
    	postRepository.findById(pid)
    			.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid));
    	
    	List<Comments> comments = commentRepository.findByPostPid(pid);
    	return comments.stream()
    			.map(comment -> CommentResponse.builder()
    					.cid(comment.getCid())
    					.pid(comment.getPost().getPid())
    					.uid(comment.getUser().getUid())
    					.content(comment.getContent())
    					.build()
    				)
    			.collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse createComment(Long pid, CommentRequest request) {
    	Posts post = postRepository.findById(pid)
    			.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + pid));
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
    	Users user = userDetails.getUser();
    	
    	Comments comment = new Comments(
    			null,
    			post,
    			user,
    			request.getContent()
    	);
    	
    	Comments savedComment = commentRepository.save(comment);
    	return CommentResponse.builder()
    			.cid(savedComment.getCid())
    			.pid(savedComment.getPost().getPid())
    			.uid(savedComment.getUser().getUid())
    			.content(savedComment.getContent())
    			.build();
    }

    @Transactional
    public CommentResponse updateComment(Long pid, Long cid, CommentRequest request) {
        Comments comment = commentRepository.findById(cid)
        		.orElseThrow(() -> new CommentNotFoundException("Comment not found with id: " + cid));
        
        if (!comment.getPost().getPid().equals(pid)) {
        	throw new CommentNotBelongToPostException("Comment Not Belong To The Post");
        }
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
    	Users currentUser = userDetails.getUser();
    	
    	if (!comment.getUser().getUid().equals(currentUser.getUid())) {
    		throw new UnauthorizedException("No permission to edit the comment");
    	}
    	
    	comment.updateComment(request.getContent());
    	
    	Comments updatedComment = commentRepository.save(comment);
    	return CommentResponse.builder()
    			.cid(updatedComment.getCid())
    			.pid(updatedComment.getPost().getPid())
    			.uid(updatedComment.getUser().getUid())
    			.content(updatedComment.getContent())
    			.build();
    }
//
//    public void deleteComment(Long commentId) {
//        commentRepository.deleteById(commentId);
//    }
}
