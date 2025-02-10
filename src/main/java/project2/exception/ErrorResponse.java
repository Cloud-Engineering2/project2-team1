package project2.exception;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

	private int status;
	private String message;
    private String error;
    private String path;
    private LocalDateTime timestamp;

    /**
     * @param status HTTP 상태 코드
     * @param message 에러 메시지
     * @param error 발생한 에러의 이름
     * @param path 에러가 발생한 요청 경로
     * @param timestamp 에러 발생 시간
     */
    public ErrorResponse(int status, String message, String error, String path, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.path = path;
        this.timestamp = timestamp;
    }
}