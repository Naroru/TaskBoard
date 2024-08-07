package chukhlantsev.oleg.taskboard.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//от пользователя мы будем принимать имя и пароль и их авторизовывать
@Data
@Schema(name = "JWT request")
public class JwtRequest {

    @Schema(description = "User's email", example = "Alex@mail.ru")
    @NotNull(message = "Username must be not null.")
    String username;

    @Schema(description = "User's password", example = "12345")
    @NotNull(message = "Password must be not null")
    String password;

}
