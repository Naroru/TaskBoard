package chukhlantsev.oleg.taskboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //включает возможность использования транзакций в спринге - используем в сервисах
@EnableCaching //включает возможность кэширования
public class TaskBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskBoardApplication.class, args);
    }

}
