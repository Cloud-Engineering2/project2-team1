package project2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project2.entity.Posts;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {
    
    // 1️ 전체 게시물 조회
    List<Posts> findAll();

    // 2️ 특정 사용자 게시물 조회
    List<Posts> findByUserUid(Long uid);

    // 3️ 특정 게시물 상세 조회
    Optional<Posts> findById(Long pid);
	
}