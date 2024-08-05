package chukhlantsev.oleg.taskboard.repository;


import chukhlantsev.oleg.taskboard.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // без nativeQuery используется JPQL, а не SQL. У него немного другой синтаксис.
    //@Param подставляет параметр в запрос
    @Query(value = """
            SELECT * FROM tasks t
            WHERE t.fk_user_id = :userID
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userID") Long userID);

    //List<Task> findAllByUserId(Long userID);
}
