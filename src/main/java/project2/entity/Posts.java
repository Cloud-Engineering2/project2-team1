//package project2.entity;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import project2.enums.MealType;
//
//@Entity
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Posts extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long pid;
//    
//    @ManyToOne
//    @JoinColumn(name = "uid")
//    private Users user;
//    
//    private String content;
//    
//    @Column(name = "meal_date")
//    private LocalDateTime mealDate;
//    
//    @Column(name = "meal_type") 
//    private MealType mealType;
//    
//    private Long calories;
//    
//    @Column(name = "image_url")
//    private String imageUrl;
//
//}



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

    // createdAt 및 updatedAt 필드 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ID를 설정하는 메서드
    public void setPid(Long pid) {
        this.pid = pid;
    }
    
    // 사용자 설정 메서드
    public void setUser(Users user) {
        this.user = user;
    }

    // createdAt 및 updatedAt Setter 메서드 추가
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

//24.02.08 경민수정ver

