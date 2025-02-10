package project2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserProfileUpdateRequest {
    private String username;
    private String bio;
}
