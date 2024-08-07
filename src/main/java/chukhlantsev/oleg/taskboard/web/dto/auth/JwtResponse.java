package chukhlantsev.oleg.taskboard.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//возвращать пользователю мы будем его имя, ай ди, а также пару - токен доступа и рефреш токен
@Data
@Schema(name = "JWT response")
public class JwtResponse {

    @Schema(description = "user's ID", example = "1")
    private Long id;

    @Schema(description = "User's email", example = "Alex@mail.ru")
    private String username;

    @Schema(description = "JWT access token")
    private String accessToken;

    @Schema(description = "JWT refresh token")
    private String refreshToken;

}
