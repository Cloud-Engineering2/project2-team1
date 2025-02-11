package project2.exception;

public class CommentNotBelongToPostException extends RuntimeException {
	public CommentNotBelongToPostException(String message) {
		super(message);
	}
}
