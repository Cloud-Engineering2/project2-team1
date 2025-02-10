package project2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import project2.dto.UserProfileUpdateRequest;
import project2.dto.UserRegistrationDto;
import project2.dto.UserResponse;
import project2.entity.Users;
import project2.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class UserController {
    
    private final UserService userService;

    @PostMapping("/api/register") // 회원 가입 API
    public ResponseEntity<String> apiRegisterUserAccount(@RequestBody UserRegistrationDto registrationDto) {
        userService.registerUserAccount(registrationDto);
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
    
}
