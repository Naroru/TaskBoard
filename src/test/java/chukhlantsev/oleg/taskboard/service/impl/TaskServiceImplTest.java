package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.task.Status;
import chukhlantsev.oleg.taskboard.domain.task.Task;
import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.repository.TaskRepository;
import chukhlantsev.oleg.taskboard.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@DisplayName("unit-testing for TaskService")
//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private  TaskRepository taskRepository;

    @Mock
    private  UserService userService;

    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    public void GetTaskById()
    {
        //given
        Long id = 1L;
        Task task = new Task();

        when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
      //when
        Task testTask = taskService.getTaskById(id);

        //then
        assertEquals(task,testTask);
        verify(taskRepository).findById(id);

    }

    @Test
    public void shouldNotGetTaskById()
    {
        //given
        Long id = 1L;

        when(taskRepository.findById(id))
                .thenReturn(Optional.empty());

        //when - then
        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(id));
        verify(taskRepository).findById(id);

    }

    @Test
    public void updateTask()
    {
        //given
        Task task = new Task();
        task.setId(1L);
        task.setStatus(Status.DONE);
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setExpirationDate(LocalDateTime.now());


        when(taskRepository.save(task))
                .thenReturn(task);
        //when
        Task testTask = taskService.update(task);

        //then
        assertEquals(task, testTask);
        assertEquals(task.getStatus(), testTask.getStatus());
        assertEquals(task.getTitle(), testTask.getTitle());
        assertEquals(task.getUser(), testTask.getUser());
        assertEquals(task.getDescription(), testTask.getDescription());
        assertEquals(task.getExpirationDate(), testTask.getExpirationDate());

    }

    @Test
    public void updateTaskWithEmptyStatus()
    {
        //given
        Task task =  generateTask();

        when(taskRepository.save(task))
                .thenReturn(task);
        //when
        Task testTask = taskService.update(task);

        //then
        assertEquals(task, testTask);
        assertEquals(task.getStatus(), Status.TODO);
        assertEquals(task.getTitle(), testTask.getTitle());
        assertEquals(task.getUser(), testTask.getUser());
        assertEquals(task.getDescription(), testTask.getDescription());
        assertEquals(task.getExpirationDate(), testTask.getExpirationDate());

    }

    @Test
    public void getAllByUserId()
    {
        //given
        Long userId = 1L;
        List<Task> expectedTasks = List.of(new Task[]
                {
                        new Task(),
                        new Task(),
                        new Task()
                });

        when(taskRepository.findAllByUserId(userId))
                .thenReturn(expectedTasks);

        //when
        List<Task> tasks = taskService.getAllByUserId(userId);

        //then
        assertEquals(expectedTasks, tasks);
        verify(taskRepository).findAllByUserId(userId);

    }

    @Test
    public void createTask()
    {
        //given
        Task task = generateTask();
        task.setStatus(null);

        User user = new User();
        user.setId(1L);

        when(taskRepository.save(task))
                .thenReturn(task);

        when(userService.getByID(user.getId()))
                .thenReturn((user));

        //when
        Task testTask = taskService.create(task,user.getId());

        //then
        assertEquals(user,task.getUser());
        assertEquals(Status.TODO, task.getStatus());
        assertEquals(task, testTask);


    }


    @Test
    public void deleteTask()
    {

        //given
        Task task = generateTask();

        when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        //when
        taskService.delete(task.getId());

        //then
        verify(taskRepository).delete(task);


    }

    private Task generateTask()
    {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Title");
        task.setDescription("Desc");
        task.setExpirationDate(LocalDateTime.now());

        return task;
    }
}