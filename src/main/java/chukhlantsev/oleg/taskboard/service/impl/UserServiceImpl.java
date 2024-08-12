package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.user.Role;
import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.repository.UserRepository;
import chukhlantsev.oleg.taskboard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getByID(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id = %d not found", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User %s not found", username)
                ));
    }

    @Override
    @Transactional
    public User update(User user) {
        //todo нужна ли в подобных методах проверка, что пользователь существует?
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User create(User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent())
            throw new IllegalStateException("User already exists");

        if(!user.getPassword().equals(user.getPasswordConfirmation()))
            throw new IllegalStateException("Password and password confirmation don't match");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        return userRepository.save(user);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.findById(id)
                .ifPresent(userRepository::delete);
    }
}
