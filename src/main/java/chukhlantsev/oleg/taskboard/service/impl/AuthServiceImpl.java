package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.service.AuthService;
import chukhlantsev.oleg.taskboard.service.UserService;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtRequest;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtResponse;
import chukhlantsev.oleg.taskboard.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        //аутентификацию сделает за нас  authenticationManager, бин которого мы определили в ApplicationConfig
        JwtResponse jwtResponse = new JwtResponse();
        //он вытащит пользователя БД через JwtUserDetailsService, захэширует присланный пользователем пароль в loginRequest
        //и сравнит его с паролем из БД
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        //если всё ОК ( не выброшено исключение ) Тогда
        User user = userService.getByUsername(loginRequest.getUsername());
        jwtResponse.setId(user.getId());
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(
                jwtTokenProvider.createAccessToken(
                        user.getId(), user.getUsername(), user.getRoles()
                )
        );
        jwtResponse.setRefreshToken(
                jwtTokenProvider.createRefreshToken(
                        user.getId(), user.getUsername()
                )
        );

        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return jwtTokenProvider.refreshUserToken(refreshToken);
    }
}
