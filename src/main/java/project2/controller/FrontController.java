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

    @GetMapping("/login")
    public String getLoginPage() {
        return "login"; // login.html 반환
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register"; // register.html 반환
    }

    @GetMapping("/users/{uid}/profile")
    public String getProfilePage(@PathVariable("uid") Long uid, Model model) {
        Users user = userService.getUserById(uid);
        List<Posts> posts = postService.getPostsByUser(uid); // 사용자의 게시물 조회
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        return "profile"; // profile.html 반환
    }

    @GetMapping("/users/{uid}/profile-edit")
    public String getProfileEditPage(@PathVariable("uid") Long uid, Model model) {
        Users user = userService.getUserById(uid);
        model.addAttribute("user", user);
        return "profile-edit"; // profile-edit.html 반환
    }
}
