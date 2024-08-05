package chukhlantsev.oleg.taskboard.web.dto.auth;

import lombok.Data;

//возвращать пользователю мы будем его имя, ай ди, а также пару - токен доступа и рефреш токен
@Data
public class JwtResponse {

    private Long id;
    private String username;

    private String accessToken;
    private String refreshToken;

}
