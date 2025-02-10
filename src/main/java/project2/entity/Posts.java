package project2.entity;

import java.time.LocalDateTime; 

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project2.enums.MealType;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @ManyToOne
    @JoinColumn(name = "uid")
    private Users user;

    private String content;

    @Column(name = "meal_date")
    private LocalDateTime mealDate;

    @Column(name = "meal_type")
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    private Long calories;

    @Column(name = "image_url")
    private String imageUrl;

    public void updatePost(String content, LocalDateTime mealDate, MealType mealType, Long calories, String imageUrl) {
    	this.content = content;
    	this.mealDate = mealDate;
    	this.mealType = mealType;
    	this.calories = calories;
    	this.imageUrl = imageUrl;

    }
}

