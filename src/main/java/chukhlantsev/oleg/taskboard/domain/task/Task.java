package chukhlantsev.oleg.taskboard.domain.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Entity
@FieldDefaults(level = PRIVATE)
@Setter
@Getter
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;

    @Column(length = 1024)
    String description;

    @Enumerated(EnumType.STRING)
    Status status;

    LocalDateTime expirationDate;


}
