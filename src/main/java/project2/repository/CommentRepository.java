package project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project2.entity.Comments;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByPostPid(Long postId);
}
