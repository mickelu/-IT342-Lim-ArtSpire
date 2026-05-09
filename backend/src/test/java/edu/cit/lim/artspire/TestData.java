package edu.cit.lim.artspire;

import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.auth.dto.RegisterRequest;
import edu.cit.lim.artspire.user.User;

import java.time.LocalDateTime;

public final class TestData {

    public static final String VALID_NAME = "Ana Reyes";
    public static final String VALID_EMAIL = "ana.reyes@example.com";
    public static final String VALID_PASSWORD = "password123";

    private TestData() {
    }

    public static RegisterRequest validRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setName(VALID_NAME);
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);
        return request;
    }

    public static LoginRequest validLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);
        return request;
    }

    public static User user(String encodedPassword) {
        User user = new User();
        user.setName(VALID_NAME);
        user.setEmail(VALID_EMAIL);
        user.setPassword(encodedPassword);
        user.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        return user;
    }
}
