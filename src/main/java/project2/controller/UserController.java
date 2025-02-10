package project2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import project2.dto.UserRegistrationDto;
import project2.service.UserService;

@RequiredArgsConstructor
@Controller
public class UserController {
    
    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) { // 회원 가입 페이지
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) { // 회원 가입 완료
        userService.save(registrationDto);
        return "redirect:/register?success";
    }
    
}
