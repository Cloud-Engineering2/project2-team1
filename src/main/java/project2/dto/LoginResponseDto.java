package project2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private int status;
    private String message;
    private String token;
    private String path;
    private String username; 
    private String timestamp;

    /**
     * @param status HTTP 상태 코드
     * @param message 응답 메시지
     * @param token JWT 토큰
     * @param path 요청 경로
     * @param username 사용자 이름
     * @param timestamp 응답 시간
    */

    public LoginResponseDto toDto(int status, String message, String token, String path, String username, String timestamp) {
        return new LoginResponseDto(status, message, token, path, username, timestamp);
    }
}
