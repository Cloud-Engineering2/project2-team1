package project2.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import project2.entity.Posts;
import project2.exception.PostNotFoundException;
import project2.Repository.PostsRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsService {
    private final PostsRepository postsRepository;

    // 1️ 전체 게시물 조회
    public List<Posts> getAllPosts() {
        return postsRepository.findAll();
    }

    // 2️ 특정 사용자 게시물 조회
    public List<Posts> getPostsByUser(Long uid) {
        List<Posts> posts = postsRepository.findByUserUid(uid);
        
        if (posts.isEmpty()) { // 만약 게시물이 없다면 예외 발생
            throw new PostNotFoundException("No posts found for user id " + uid);
        }
        
        return posts;
    }


    // 3️ 특정 게시물 상세 조회
    public Posts getPostById(Long pid) {
        return postsRepository.findById(pid)
                .orElseThrow(() -> new PostNotFoundException(pid)); // 예외 발생
    }
}
