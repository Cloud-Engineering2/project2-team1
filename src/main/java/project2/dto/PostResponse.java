package project2.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import project2.entity.Posts;
import project2.enums.MealType;

@Getter
@Builder
public class PostResponse {
	private final Long pid;
	private final Long uid;
	private final String username;
	private final String profileImage;
    private final String content;
    private final LocalDateTime mealDate;
    private final MealType mealType;
    private final Long calories;
    private final List<String> imageUrlList;
    
    public PostResponse(Long pid, Long uid, String username, 
    		String profileImage, String content, LocalDateTime mealDate, 
    		MealType mealType, Long calories, List<String> imageUrlList) {
    	this.pid = pid;
    	this.uid = uid;
    	this.username = username;
    	this.profileImage = profileImage;
    	this.content = content;
    	this.mealDate = mealDate;
    	this.mealType = mealType;
    	this.calories = calories;
    	this.imageUrlList = imageUrlList;
    }
    
    public static PostResponse toDto(Posts post) {
    	return PostResponse.builder()
	        .pid(post.getPid())
	        .uid(post.getUser().getUid())
	        .username(post.getUser().getUsername())
	        .profileImage(post.getUser().getProfileImageUrl())
	        .content(post.getContent())
	        .mealDate(post.getMealDate())
	        .mealType(post.getMealType())
	        .calories(post.getCalories())
	        .imageUrlList(post.getImageUrls())
	        .build();
    }
}