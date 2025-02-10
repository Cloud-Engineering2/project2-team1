package project2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import project2.dto.UserRegistrationDto;
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
    
}
