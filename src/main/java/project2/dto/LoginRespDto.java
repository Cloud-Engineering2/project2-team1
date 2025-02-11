package project2.dto;

import lombok.Getter;

@Getter
public class LoginRespDto {
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

    public LoginRespDto(int status, String message, String token, String path, String username, String timestamp) {
        this.status = status;
        this.message = message;
        this.token = token;
        this.path = path;
        this.username = username;
        this.timestamp = timestamp;
    }
}
