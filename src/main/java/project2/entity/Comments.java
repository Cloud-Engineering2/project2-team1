package project2.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Comments {
    @Id
    private Long cid;
    private Long pid;
    private Long uid;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
