package chukhlantsev.oleg.taskboard.domain.user;

public enum Role {
//начинаем с ROLE_ , чтобы спринг бут нормально взаимодействовал с ролями для проверок, секьюрити
    ROLE_ADMIN,
    ROLE_USER;
}
