package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.task.Status;
import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.repository.TaskRepository;
import chukhlantsev.oleg.taskboard.service.TaskService;
import chukhlantsev.oleg.taskboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "TaskService::getTaskById")  //ключ указывать необязательно, т.к. 1 параметр и он является ключом
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task with id = %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    //не кэшируем, потому что обновлять кэш придется при каждом изменении, добавлении задачи\удалении пользователя
    //но на самом деле можно
    public List<Task> getAllByUserId(Long userId) {
        return  taskRepository.findAllByUserId(userId);
    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getTaskById", key = "#task.id")
    public Task update(Task task) {

        if(task.getStatus() == null)
            task.setStatus(Status.TODO);

        return  taskRepository.save(task);

    }

    @Override
    @Transactional
    @CachePut(value = "TaskService::getTaskById", key = "#task.id")
    public Task create(Task task, Long userId) {

        task.setStatus(Status.TODO);
        task.setUser(userService.getByID(userId));

        return  taskRepository.save(task);

    }

    @Override
    @Transactional
    @CacheEvict(value = "TaskService::getTaskById") //ключ указывать необязательно, т.к. 1 параметр и он является ключом
    public void delete(Long id) {

        Optional<Task> taskOptional = taskRepository.findById(id);
        taskOptional.ifPresent(taskRepository::delete);

    }
}
