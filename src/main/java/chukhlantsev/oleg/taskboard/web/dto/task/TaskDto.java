package chukhlantsev.oleg.taskboard.web.dto.task;

import chukhlantsev.oleg.taskboard.domain.task.Status;
import chukhlantsev.oleg.taskboard.web.dto.validation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class TaskDto {

    @NotNull(message = "Id must be not null", groups = onUpdate.class)
    @Null(message = "Id must be  null", groups = onCreate.class)
    Long id;

    @NotBlank(message = "Title must be not blank")
    @Length(max = 255, message = "Title must be smaller than 255 symbols")
    String title;

    @Length(max = 255, message = "Description must be smaller than 255 symbols")
    String description;

    Status status;


    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime expirationDate;

}
