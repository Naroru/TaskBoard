package chukhlantsev.oleg.taskboard.web.dto.user;

import chukhlantsev.oleg.taskboard.web.dto.validation.OnCreate;
import chukhlantsev.oleg.taskboard.web.dto.validation.OnUpdate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {

    @NotNull(groups = OnUpdate.class, message = "Id must be ton null")
    @Null(groups = OnCreate.class, message = "Id ,must be null")
    private Long id;

    @NotNull(message = "Name must be not null")
    @Length(max = 255, message = "Name must be smaller than 255 symbols")
    private String name;

    @NotNull(message = "Name must be not null")
    @Length(max = 255, message = "Name must be smaller than 255 symbols")
    private String username;

    //пароль в json будет доступен только для записиб т.е. не будем возврашать его клиенту
    //проверка на длину не нужна, т.к. будем хэшировать
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null")
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = OnCreate.class)
    private String passwordConfirmation;

}
