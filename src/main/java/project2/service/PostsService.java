package project2.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import project2.entity.Posts;
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

}
