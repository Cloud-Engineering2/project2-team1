package project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project2.entity.Posts;

public interface PostRepository extends JpaRepository<Posts, Long> {
}

//24.02.08 경민수정ver