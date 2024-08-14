package chukhlantsev.oleg.taskboard.web.controller;

import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.service.TaskService;
import chukhlantsev.oleg.taskboard.service.UserService;
import chukhlantsev.oleg.taskboard.web.dto.task.TaskDto;
import chukhlantsev.oleg.taskboard.web.dto.user.UserDto;
import chukhlantsev.oleg.taskboard.web.dto.validation.OnCreate;
import chukhlantsev.oleg.taskboard.web.dto.validation.OnUpdate;
import chukhlantsev.oleg.taskboard.web.mappers.TaskMapper;
import chukhlantsev.oleg.taskboard.web.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@Validated
@Tag(name = "User controller", description = "User API")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public UserController(UserService userService, UserMapper userMapper, TaskService taskService, TaskMapper taskMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public UserDto getById(@PathVariable Long id)
    {
        User user = userService.getByID(id);
        return userMapper.toDTO(user);
    }


    @PutMapping
    @Operation(summary = "Update existing user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserDto updateUser(@Validated(OnUpdate.class) @RequestBody UserDto dto)
    {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);

        return userMapper.toDTO(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteById(@PathVariable Long id)
    {

        userService.delete(id);
    }

    @GetMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<TaskDto> getUserTasks(@PathVariable Long id) {

        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDTO(tasks);

    }

    @PostMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDto createTaskForUser(@PathVariable long id, @Validated(OnCreate.class) @RequestBody TaskDto dto)
    {
        Task task = taskMapper.toEntity(dto);
        Task newTask = taskService.create(task, id);
        return taskMapper.toDTO(newTask);
    }
}
