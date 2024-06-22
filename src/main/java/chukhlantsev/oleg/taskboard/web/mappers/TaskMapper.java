package chukhlantsev.oleg.taskboard.web.mappers;

import chukhlantsev.oleg.taskboard.web.dto.task.TaskDto;
import chukhlantsev.oleg.taskboard.domain.task.Task;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
public interface TaskMapper {

    Task toEntity(TaskDto dto);

    TaskDto toDTO(Task task);

    List<TaskDto> toDTO (List<Task> tasks);

}
