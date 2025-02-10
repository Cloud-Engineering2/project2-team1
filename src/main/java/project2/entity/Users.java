//package project2.entity;
//
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//public class Users extends BaseTimeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long uid;
//    
//    @Column(unique = true)
//    private String username;
//    
//    private String password;
//    
//    @Column(unique = true)
//    private String email;
//    
//    @Column(name = "profile_image_url")
//    private String profileImageUrl;
//    
//    private String bio;
//    
//}




package project2.entity;

import java.time.LocalDateTime; // Import 추가

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    private String bio;

    // createdAt 및 updatedAt 필드 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // ID를 설정하는 메서드
    public void setUid(Long uid) {
        this.uid = uid;
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