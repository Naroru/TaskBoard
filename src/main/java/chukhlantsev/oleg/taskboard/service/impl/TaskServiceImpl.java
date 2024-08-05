package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.task.Status;
import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.repository.TaskRepository;
import chukhlantsev.oleg.taskboard.service.TaskService;
import chukhlantsev.oleg.taskboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task with id = %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllByUserId(Long id) {
        return  taskRepository.findAllByUserId(id);
    }

    @Override
    @Transactional
    public Task update(Task task) {

        if(task.getStatus() == null)
            task.setStatus(Status.TODO);

        return  taskRepository.save(task);

    }

    @Override
    @Transactional
    public Task create(Task task, Long userId) {

        task.setStatus(Status.TODO);
        task.setUser(userService.getByID(userId));

        return  taskRepository.save(task);

    }

    @Override
    @Transactional
    public void delete(Long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);
        taskOptional.ifPresent(taskRepository::delete);

    }
}
