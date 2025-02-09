package project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project2.entity.Posts;

@Repository
public interface PostRepository extends JpaRepository<Posts, Long> {

}
