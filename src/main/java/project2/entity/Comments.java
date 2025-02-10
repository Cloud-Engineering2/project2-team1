package project2.entity;

import jakarta.persistence.*;
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

    public void setComment(String content) {
        this.comment = content;
    }

    public void setPost(Posts post) {
        this.post = post;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
