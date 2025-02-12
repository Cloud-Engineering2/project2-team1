package project2.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
    
    @Enumerated(EnumType.STRING) // Enum 값을 String으로 저장
    @Column(name = "meal_type")
    private MealType mealType;
    
    private Long calories;
    
    @ElementCollection
    @CollectionTable(name = "post_image_urls", joinColumns = @JoinColumn(name = "pid"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();
    
    // 연관된 댓글들을 Cascade 및 orphanRemoval 옵션으로 관리
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comments> comments = new ArrayList<>();

    public void updatePost(String content, LocalDateTime mealDate, MealType mealType, Long calories, List<String> imageUrls) {
        this.content = content;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.calories = calories;
        this.imageUrls = imageUrls;
    }
}
