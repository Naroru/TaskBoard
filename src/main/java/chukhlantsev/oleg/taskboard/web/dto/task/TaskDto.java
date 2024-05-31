package chukhlantsev.oleg.taskboard.web.dto.task;

import chukhlantsev.oleg.taskboard.domain.task.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    Long id;
    String title;
    String description;
    Status status;
    LocalDateTime expirationDate;

}
