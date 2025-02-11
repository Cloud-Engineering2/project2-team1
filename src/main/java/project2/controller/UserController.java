package project2.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import project2.dto.UserProfileUpdateRequest;
import project2.dto.UserRegistrationDto;
import project2.dto.UserResponse;
import project2.entity.Posts;
import project2.entity.Users;
import project2.service.PostService;
import project2.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {
    
    private final UserService userService;
    private final PostService postService;

    @PostMapping(value = "/register", consumes = {"multipart/form-data"} ) // 회원 가입 API 
    public ResponseEntity<String> apiRegisterUserAccount(@RequestPart("user") UserRegistrationDto registrationDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.registerUserAccount(registrationDto, profileImage);
        return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "") // 클라이언트가 JWT 삭제하도록 빈 값 반환
                .body("로그아웃 되었습니다.");
    }
    
    @GetMapping("/users/{uid}")
    public ResponseEntity<Users> getUserProfile(@PathVariable("uid") Long uid) {
        Users user = userService.getUserById(uid);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
    
    @PutMapping("/users/{uid}")
    public ResponseEntity<UserResponse> updateUserProfile(
            @PathVariable("uid") Long uid,
            @RequestParam("user") String userJson,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        UserProfileUpdateRequest request;
        
        try {
            request = objectMapper.readValue(userJson, UserProfileUpdateRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        UserResponse updatedUser = userService.updateUserProfile(uid, request, image);
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/users/{uid}")
    public ResponseEntity<String> deleteUser(@PathVariable("uid") Long uid) {
        userService.deleteUser(uid);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
    
}
