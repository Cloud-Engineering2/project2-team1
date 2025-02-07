package project2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Posts {
    @Id    
    private Long pid;
    private Long uid;
    private String content;
    private String mealDate;
    private String mealType;
    private Integer calories;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
