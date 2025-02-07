package project2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    private MealType mealType;
    
    private Long calories;
    
    @Column(name = "image_url")
    private String imageUrl;

}
