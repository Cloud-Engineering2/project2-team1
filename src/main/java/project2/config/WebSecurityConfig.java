package project2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import lombok.RequiredArgsConstructor;
import project2.repository.UserRepository;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain sfc(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize
                .requestMatchers("/api/register").permitAll() // 회원 가입은 누구나 액세스 가능
                .requestMatchers("/api/login").permitAll() // 회원 가입은 누구나 액세스 가능
                .requestMatchers("/style/**").permitAll() // 정적 파일은 누구나 액세스 가능
                // 게시물 목록, 작성
                .requestMatchers("/api/posts").permitAll()
                // 특정 사용자 게시물 목록
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/users/[A-z0-9]+/posts")).authenticated()
                // 게시물 상세 조회, 수정, 삭제
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/posts/[A-z0-9]+")).authenticated()
                // 회원 탈퇴, 프로필 조회, 프로필 수정
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/users/[A-z0-9]+")).authenticated()
                // 로그아웃
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/logout")).authenticated()
                // 댓글 목록, 작성
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/posts/[A-z0-9]+/comments")).authenticated()
                // 댓글 수정, 삭제
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/posts/[A-z0-9]+/comments/[A-z0-9]+")).authenticated()
                // 로그인 테스트
                .requestMatchers(RegexRequestMatcher.regexMatcher("/api/test")).authenticated()
                .anyRequest().permitAll() // 그 외에는 모든 요청 허용
        ).formLogin(withDefaults()) // 로그인 페이지는 기본 제공되는 로그인 페이지 사용
        .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (개발 환경에서만)
        .formLogin(form -> form.disable())
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 상태 관리 및 세션 사용 안함.
        .httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 사용 안함.
        .with(new CustomDsl(), dsl -> dsl.flag(true)) // 새 CustomDsl() 클래스의 개체 dsl을 만들어서 그 내부의 flag를 true로 설정.
        ;
                
        return http.build();
    }

        // Domain Specific Language.
    public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity>{
    	
    	private boolean flag;
    	
    	@Override
    	public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, userRepository);

            jwtAuthenticationFilter.setFilterProcessesUrl("/api/login"); // 로그인 URL 설정
        	http 
        	.addFilter(jwtAuthenticationFilter) // 실제로 인증할 필터 삽입
        	.addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository)); // 권한 부여 필터 삽입
            
    	}

		public CustomDsl flag(boolean b) {
			this.flag = b;
			return new CustomDsl();
		}
    	
    }
}
