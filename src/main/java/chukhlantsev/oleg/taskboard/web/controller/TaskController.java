package chukhlantsev.oleg.taskboard.web.controller;

import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.service.TaskService;
import chukhlantsev.oleg.taskboard.web.dto.task.TaskDto;
import chukhlantsev.oleg.taskboard.web.dto.validation.OnUpdate;
import chukhlantsev.oleg.taskboard.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/tasks")
@Validated
@Tag(name = "Task controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper mapper;

    public TaskController(TaskService taskService, TaskMapper mapper) {
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getById(@PathVariable Long id)
    {
        Task task = taskService.getTaskById(id);
        return mapper.toDTO(task);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto)
    {
        Task task = mapper.toEntity(dto);
        Task updatedtask = taskService.update(task);
        return mapper.toDTO(updatedtask);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteById(@PathVariable Long id)
    {
        taskService.delete(id);
    }
}
