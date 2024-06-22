package chukhlantsev.oleg.taskboard.web.mappers;

import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto dto);

    UserDto toDTO(User  user);

}
