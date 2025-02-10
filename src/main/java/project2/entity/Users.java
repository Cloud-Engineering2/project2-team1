package project2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    
    public Users updateProfile(String username, String bio, String profileImageUrl) {
        return new Users(
                username != null ? username : this.username,
                this.password,
                this.email,
                profileImageUrl != null ? profileImageUrl : this.profileImageUrl,
                bio != null ? bio : this.bio
        );
    }
    
}

