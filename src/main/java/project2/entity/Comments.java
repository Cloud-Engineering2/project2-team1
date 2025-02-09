package project2.entity;

import java.time.LocalDateTime; 

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comments extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    
    @ManyToOne
    @JoinColumn(name = "pid")
    private Posts post;
    
    @ManyToOne
    @JoinColumn(name = "uid")
    private Users user;
    
    private String comment;

    // createdAt 및 updatedAt 필드 추가
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getter 및 Setter 메서드 추가
    public void setComment(String content) {
        this.comment = content;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

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