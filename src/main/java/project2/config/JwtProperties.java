package project2.config;

public interface JwtProperties {

    String SECRET = "jwt"; 	// 서명에 들어갈 시크릿.
    int ACCESS_TOKEN_EXPIRATION_TIME = 60*60*24*300; // 유지되는 시간(밀리초)
    int REFRESH_TOKEN_EXPIRATION_TIME = 60*60*24*1000; // 유지되는 시간(밀리초)
    String TOKEN_PREFIX = "Bearer "; // 반드시 삽입되어야 함.
    String HEADER_STRING = "Authorization"; // 반드시 사용해야 함.

}
