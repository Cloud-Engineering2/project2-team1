package project2.config;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import project2.dto.LoginReqDto;
import project2.dto.LoginRespDto;
import project2.exception.UserNotFoundException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
	LoginReqDto loginReqDTO = null; // username, password를 보관할 객체

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        ObjectMapper om = new ObjectMapper(); // json을 객체로 매핑하기 위한 객체
        
        try {
            // json을 읽어서 login request dto 객체 형식으로 변환한다.
            loginReqDTO = om.readValue(request.getInputStream(), LoginReqDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword());
        
		Authentication auth = null;
        // Authentication 객체 생성
        try {
			 auth = authenticationManager.authenticate(authToken);
		} catch (AuthenticationException e) { // 로그인 실패 시 예외 처리
			throw new UserNotFoundException("ID 또는 암호를 확인하십시오.");
		}
        return auth;
    };

    @Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, 
			Authentication authResult)	{
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtAccToken = generateAccessToken(principalDetails);
		String jwtRefToken = generateRefreshToken(principalDetails);
		
		String accessToken = JwtProperties.TOKEN_PREFIX + jwtAccToken;
		response.addHeader(JwtProperties.HEADER_STRING, accessToken);
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtRefToken);

		// Response Body에 토큰 및 응답 메시지를 담아서 전송
		LoginRespDto loginRespDto = new LoginRespDto(HttpServletResponse.SC_OK, "로그인 성공", accessToken, request.getRequestURI(), loginReqDTO.getUsername() ,LocalDateTime.now().toString());
		try {
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_OK);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(new ObjectMapper().writeValueAsString(loginRespDto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public String generateAccessToken(PrincipalDetails principalDetails) {
		return JWT.create().withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME))
				.withClaim("uid", principalDetails.getUsers().getUid())
				.withClaim("uname", principalDetails.getUsers().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
	public String generateRefreshToken(PrincipalDetails principalDetails) {
		return JWT.create().withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME))
				.withClaim("uid", principalDetails.getUsers().getUid())
				.withClaim("uname", principalDetails.getUsers().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
    
}
