package chukhlantsev.oleg.taskboard.web.controller;

import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.service.TaskService;
import chukhlantsev.oleg.taskboard.web.dto.task.TaskDto;
import chukhlantsev.oleg.taskboard.web.dto.validation.OnUpdate;
import chukhlantsev.oleg.taskboard.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public TaskDto getById(@PathVariable Long id)
    {
        Task task = taskService.getTaskById(id);
        return mapper.toDTO(task);
    }

    @PutMapping
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto)
    {
        Task task = mapper.toEntity(dto);
        Task updatedtask = taskService.update(task);
        return mapper.toDTO(updatedtask);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id)
    {
        taskService.delete(id);
    }
}
