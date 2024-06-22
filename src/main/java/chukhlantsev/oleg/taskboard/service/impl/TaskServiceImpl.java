package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.repository.TaskRepository;
import chukhlantsev.oleg.taskboard.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task with id = %d not found", id)));
    }

    @Override
    public List<Task> getAllByUserId(Long id) {
        return  taskRepository.findAllByUserId(id);
    }

    @Override
    public Task update(Task task) {
        return  taskRepository.save(task);
    }

    @Override
    public Task create(Task task, Long userId) {
        return null;
        //return taskRepository.save(task, userID);
    }

    @Override
    public void delete(Long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);
        taskOptional.ifPresent(taskRepository::delete);

    }
}
