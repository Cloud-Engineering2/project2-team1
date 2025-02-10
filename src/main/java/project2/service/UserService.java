package project2.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import project2.dto.UserRegistrationDto;
import project2.entity.Users;
import project2.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public Users save(UserRegistrationDto registrationDto) { // 회원 정보 저장
        Users user = new Users();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setEmail(registrationDto.getEmail());
        // 프로필 이미지 url 정보 저장용 코드 작성 필요
        return userRepository.save(user);
    }


}
