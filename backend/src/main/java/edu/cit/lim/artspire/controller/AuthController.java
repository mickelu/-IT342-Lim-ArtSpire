package edu.cit.lim.artspire.controller;

import edu.cit.lim.artspire.dto.AuthResponse;
import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.dto.RegisterRequest;
import edu.cit.lim.artspire.facade.AuthFacade;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthFacade authFacade;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request){
        return authFacade.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
        return authFacade.login(request);
    }
}
