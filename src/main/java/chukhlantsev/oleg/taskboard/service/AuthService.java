package chukhlantsev.oleg.taskboard.service;

import chukhlantsev.oleg.taskboard.web.dto.auth.JwtRequest;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtResponse;

public interface AuthService {

        JwtResponse login(
                JwtRequest loginRequest
        );

        JwtResponse refresh(
                String refreshToken
        );


}
