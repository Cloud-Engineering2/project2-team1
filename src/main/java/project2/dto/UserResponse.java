package project2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project2.entity.Users;

@Getter
@Builder
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 포함하는 생성자 추가
public class UserResponse {
    private Long uid;
    private String username;
    private String email;
    private String bio;
    private String profileImageUrl;
    
    public static UserResponse toDto(Users user) {
    	return UserResponse.builder()
		        .uid(user.getUid())
		        .username(user.getUsername())
		        .email(user.getEmail())
		        .bio(user.getBio())
		        .profileImageUrl(user.getProfileImageUrl())
		        .build();
    }
}
