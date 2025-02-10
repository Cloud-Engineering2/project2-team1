package project2.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import project2.enums.MealType;

@Getter
@Builder
public class PostResponse {
	private final Long pid;
    private final Long uid;
    private final String content;
    private final LocalDateTime mealDate;
    private final MealType mealType;
    private final Long calories;
    private final String imageUrl;
}
