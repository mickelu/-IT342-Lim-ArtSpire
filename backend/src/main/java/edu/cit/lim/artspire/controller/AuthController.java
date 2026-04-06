package edu.cit.lim.artspire.controller;

import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.dto.RegisterRequest;
import edu.cit.lim.artspire.facade.AuthFacade;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthFacade authFacade;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request){
        return authFacade.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return authFacade.login(request);
    }
}