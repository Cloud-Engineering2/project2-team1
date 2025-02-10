package project2.config;

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

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        
        ObjectMapper om = new ObjectMapper(); // json을 객체로 매핑하기 위한 객체
        LoginReqDto loginReqDTO = null; // username, password를 보관할 객체
        
        try {
            // json을 읽어서 login request dto 객체 형식으로 변환한다.
            loginReqDTO = om.readValue(request.getInputStream(), LoginReqDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword());
        
        // Authentication 객체 생성
        Authentication auth = authenticationManager.authenticate(authToken);

        return auth;
    };

    @Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, 
			Authentication authResult)	{
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtAccToken = generateAccessToken(principalDetails);
		String jwtRefToken = generateRefreshToken(principalDetails);
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtAccToken);
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtRefToken);
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
