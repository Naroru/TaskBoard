package chukhlantsev.oleg.taskboard.repository;

import chukhlantsev.oleg.taskboard.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
/*
    @Query(value = """
            SELECT exists ( 
            SELECT 1 
            FROM users_tasks 
                     WHERE users_tasks.user_id = :userID
                     AND 
                         users_tasks.task_id = :taskID)

            """, nativeQuery = true)
    boolean isTaskOwner(@Param("userID") Long userID, @Param("taskID") Long taskID);
*/


}
