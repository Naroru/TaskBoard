package chukhlantsev.oleg.taskboard.service.impl;

import chukhlantsev.oleg.taskboard.service.AuthService;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtRequest;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtResponse;

public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
