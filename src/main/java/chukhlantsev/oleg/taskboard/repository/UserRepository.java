package chukhlantsev.oleg.taskboard.repository;

import chukhlantsev.oleg.taskboard.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = """
            SELECT 1 
            FROM tasks 
                     WHERE tasks.fk_user_id = :userID
                     AND 
                         tasks.id = :taskID

            """, nativeQuery = true)
    boolean isTaskOwner(@Param("userID") Long userID, @Param("taskID") Long taskID);



}
