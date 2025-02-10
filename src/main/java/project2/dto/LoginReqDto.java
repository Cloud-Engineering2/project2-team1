package project2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginReqDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}