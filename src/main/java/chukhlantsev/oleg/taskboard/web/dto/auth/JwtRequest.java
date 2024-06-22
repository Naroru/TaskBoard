package chukhlantsev.oleg.taskboard.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JwtRequest {

    @NotNull(message = "Username must be not null.")
    String username;

    @NotNull(message = "Password must be not null")
    String password;

}
