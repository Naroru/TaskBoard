package chukhlantsev.oleg.taskboard.service;

import chukhlantsev.oleg.taskboard.domain.user.User;

public interface UserService {

    User getByID(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);


    void delete(Long id);


}
