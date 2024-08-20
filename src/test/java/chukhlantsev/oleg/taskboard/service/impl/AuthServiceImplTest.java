package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import chukhlantsev.oleg.taskboard.domain.user.Role;
import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.service.UserService;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtRequest;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtResponse;
import chukhlantsev.oleg.taskboard.web.security.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("Unit test for AuthService")
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private  JwtTokenProvider jwtTokenProvider;

    @Mock
    private  UserService userService;

    @Mock
    private  AuthenticationManager authenticationManager;

    @InjectMocks
    AuthServiceImpl authService;

    @Test

    public void loginWithCorrectUser()
    {
        //given
        String username = "username";
        String password = "password";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        Set<Role> roles = Collections.emptySet();
        Long userID = 1L;

        JwtRequest loginRequest = new JwtRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        user.setId(userID);
        user.setRoles(roles);

        when(userService.getByUsername(loginRequest.getUsername()))
                .thenReturn(user);

        when(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles()))
                .thenReturn(accessToken);

        when(jwtTokenProvider.createRefreshToken(
                user.getId(), user.getUsername()))
                .thenReturn(refreshToken);


        JwtResponse expected = new JwtResponse();
        expected.setId(userID);
        expected.setAccessToken(accessToken);
        expected.setRefreshToken(refreshToken);
        expected.setUsername(username);


        //when
        JwtResponse response = authService.login(loginRequest);

        //then
        assertEquals(expected, response);
        verify(authenticationManager)
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(),
                                loginRequest.getPassword())
                );
    }

    @Test
    void loginWithIncorrectUsername() {
        //given
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);
        User user = new User();
        user.setUsername(username);

        when(userService.getByUsername(username))
                .thenThrow(ResourceNotFoundException.class);

        //when-then
        assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
        verifyNoInteractions(jwtTokenProvider);

    }

    @Test
    void refresh() {
        //given
        String refreshToken = "refreshToken";
        String accessToken = "accessToken";
        String newRefreshToken = "newRefreshToken";

        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);

        when(jwtTokenProvider.refreshUserToken(refreshToken))
                .thenReturn(response);

        //when
        JwtResponse jwtResponse = authService.refresh(refreshToken);

        //then
        verify(jwtTokenProvider).refreshUserToken(refreshToken);
        assertEquals(jwtResponse, response);
    }

}