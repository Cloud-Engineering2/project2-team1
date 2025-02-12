package project2.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import project2.entity.Posts;
import project2.entity.Users;
import project2.service.PostService;
import project2.service.UserService;

@RequiredArgsConstructor
@Controller
public class FrontController {

    private final UserService userService;
    private final PostService postService;

    @GetMapping("/login") // 로그인
    public String getLoginPage() {
        return "login"; // login.html 반환
    }

    @GetMapping("/register") // 회원 가입
    public String getRegisterPage() {
        return "register"; // register.html 반환
    }

    @GetMapping("/test") // 로그인 테스트
    public String getTestPage() {
        return "test"; // test.html 반환
    }

    @GetMapping("/profile/{uid}")
    public String getProfilePage(@PathVariable("uid") Long uid, Model model) {
        Users user = userService.getUserById(uid);
        List<Posts> posts = postService.getPostsByUser(uid); // 사용자의 게시물 조회
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "profile"; // profile.html 반환
    }

    @GetMapping("/profile-edit/{uid}")
    public String getProfileEditPage(@PathVariable("uid") Long uid, Model model) {
        Users user = userService.getUserById(uid);
        model.addAttribute("user", user);
        return "profile-edit"; // profile-edit.html 반환
    }

    @GetMapping("/post-list")
    public String getPosts(Model model) {
//        List<Posts> posts = postService.getAllPosts(); // 서비스에서 게시글 목록 조회
//        model.addAttribute("posts", posts); // Thymeleaf로 데이터 전달
        return "post-list"; // `post-list.html` 렌더링
    }

    @GetMapping("/detail/{pid}")
    public String getPostDetailPage(@PathVariable("pid") Long pid, Model model) {
        Posts post = postService.getPostById(pid);
        model.addAttribute("post", post);
        return "post-detail"; // post-detail.html 템플릿 반환
    }
    
    @GetMapping("/post-create")
    public String getPostCreatePage() {
        return "post-create"; // post-create.html 반환
    }
    @GetMapping("/post-edit/{pid}") // 게시글 수정 페이지 추가
    public String getPostEditPage(@PathVariable("pid") Long pid, Model model) {
        Posts post = postService.getPostById(pid);
        model.addAttribute("post", post);
        return "post-edit"; // post-edit.html 반환
    }
}
