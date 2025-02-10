package project2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project2.entity.Posts;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {
	List<Posts> findByUserUid(Long uid);
}
