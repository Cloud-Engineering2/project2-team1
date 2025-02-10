package project2.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long pid) {
        super("Post with id " + pid + " not found.");
    }

    public PostNotFoundException(String message) { // 기존 생성자 수정
        super(message); // 부모 클래스 RuntimeException에 메시지를 전달
    }
}