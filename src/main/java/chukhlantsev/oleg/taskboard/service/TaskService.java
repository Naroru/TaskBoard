package chukhlantsev.oleg.taskboard.service;
import chukhlantsev.oleg.taskboard.domain.task.Task;

import java.util.List;

public interface TaskService {

    Task getTaskById(Long id);

    List<Task> getAllByUserId(Long id);

    Task update (Task task);

    Task create (Task  task, Long userID);

    void delete(Long id);

}
