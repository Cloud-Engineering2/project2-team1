package project2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain sfc(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                .requestMatchers("/register").permitAll() // 회원 가입은 누구나 액세스 가능
                .requestMatchers("/style/**").permitAll() // 정적 파일은 누구나 액세스 가능
                .anyRequest().authenticated() // 그 외의 페이지는 인증 필요
        ).formLogin(withDefaults()); // 로그인 페이지는 기본 제공되는 로그인 페이지 사용
        return http.build();
    }
}
