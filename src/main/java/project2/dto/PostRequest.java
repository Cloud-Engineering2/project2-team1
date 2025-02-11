package project2.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import project2.enums.MealType;

@Getter
@Builder
@JsonDeserialize(builder = PostRequest.PostCreateRequestBuilder.class)
public class PostRequest {
	@NotNull
	private final Long uid;
	
	@NotNull
	private final String content;
	
	@NotNull
	private final LocalDateTime mealDate;
	
	@NotNull
	private final MealType mealType;
	
	@NotNull
	private final Long calories;
	

    @JsonPOJOBuilder(withPrefix = "")
    public static class PostCreateRequestBuilder {
    }
}
