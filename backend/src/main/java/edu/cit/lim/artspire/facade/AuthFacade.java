package edu.cit.lim.artspire.facade;

import edu.cit.lim.artspire.dto.AuthResponse;
import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.dto.RegisterRequest;
import edu.cit.lim.artspire.service.AuthService;

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
