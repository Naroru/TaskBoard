package chukhlantsev.oleg.taskboard.web.dto.user;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String username;
    private String password;
    private String passwordConfirmation;

}
