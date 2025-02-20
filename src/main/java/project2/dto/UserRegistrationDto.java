package project2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    private String profileImageUrl;
    private String bio;

}
