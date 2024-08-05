package chukhlantsev.oleg.taskboard.web.controller;

import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.service.AuthService;
import chukhlantsev.oleg.taskboard.service.UserService;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtRequest;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtResponse;
import chukhlantsev.oleg.taskboard.web.dto.user.UserDto;
import chukhlantsev.oleg.taskboard.web.dto.validation.OnCreate;
import chukhlantsev.oleg.taskboard.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest request)
    {
        return authService.login(request);
    }

    @PostMapping("/registration")
    public UserDto registration(@Validated(OnCreate.class) @RequestBody UserDto userDto)
    {
        User user = userMapper.toEntity(userDto);
        userService.create(user);

        return userMapper.toDTO(user);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(String refreshToken)
    {
        return  authService.refresh(refreshToken);
    }
}
