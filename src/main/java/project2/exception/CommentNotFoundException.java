package project2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {
	public CommentNotFoundException(String message) {
		super(message);
	}
	
	public CommentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
