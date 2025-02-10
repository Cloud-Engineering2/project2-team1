package project2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import project2.dto.UserRegistrationDto;
import project2.entity.Users;
import project2.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class UserController {
    
    private final UserService userService;

    @PostMapping("/api/register") // 회원 가입 API
    public ResponseEntity<String> apiRegisterUserAccount(@RequestBody UserRegistrationDto registrationDto,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        userService.registerUserAccount(registrationDto, profileImage);
        return new ResponseEntity<>("회원 가입이 완료되었습니다.", HttpStatus.CREATED);
    }

    @GetMapping("/api/test")
    public String test() {
        return "test";
    }
    
    @GetMapping("/users/{uid}")
    public ResponseEntity<Users> getUserProfile(@PathVariable("uid") Long uid) {
        Users user = userService.getUserById(uid);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
}
