package chukhlantsev.oleg.taskboard.repository;


import chukhlantsev.oleg.taskboard.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // без nativeQuery используется JPQL, а не SQL. У него немного другой синтаксис.
    @Query(value = """
            SELECT * FROM task t
            JOIN users_tasks ut ON ut.task_id = t.id
            WHERE ut.user_id = :userID
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userID") Long userID);

}
