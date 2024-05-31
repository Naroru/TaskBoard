package chukhlantsev.oleg.taskboard.web.dto.auth;

import lombok.Data;

@Data
public class JwtRequest {

    String username;

    String password;

}
