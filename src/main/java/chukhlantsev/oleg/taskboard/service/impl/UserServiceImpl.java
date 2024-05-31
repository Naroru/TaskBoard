package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.repository.UserRepository;
import chukhlantsev.oleg.taskboard.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getByID(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id = %d not found", id)));
    }

    @Override
    public User getByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User %s not found", username)
                ));
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean isTaskOwner(Long userID, Long taskID) {
        return userRepository.isTaskOwner(userID, taskID);
    }

    @Override
    public void delete(Long id) {
        userRepository.findById(id)
                .ifPresent(userRepository::delete);
    }
}
