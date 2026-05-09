package edu.cit.lim.artspire.auth;

import edu.cit.lim.artspire.auth.dto.AuthResponse;
import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.auth.dto.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {

    @Autowired
    private AuthService authService;

    public AuthResponse register(RegisterRequest request){
        return authService.register(request);
    }

    public AuthResponse login(LoginRequest request){
        return authService.login(request);
    }
}
