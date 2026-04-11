package edu.cit.lim.artspire.service;

import edu.cit.lim.artspire.dto.AuthResponse;
import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.dto.RegisterRequest;
import edu.cit.lim.artspire.factory.UserFactory;
import edu.cit.lim.artspire.model.User;
import edu.cit.lim.artspire.repository.UserRepository;
import edu.cit.lim.artspire.strategy.LoginStrategy;
import edu.cit.lim.artspire.strategy.EmailLoginStrategy;
import edu.cit.lim.artspire.observer.UserObserver;
import edu.cit.lim.artspire.observer.NotificationObserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Observer Pattern
    private UserObserver observer = new NotificationObserver();

    public AuthResponse register(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new ResponseStatusException(BAD_REQUEST, "Email already exists");
        }

        User user = UserFactory.createUser(
                request.getName(),
                request.getEmail(),
                encoder.encode(request.getPassword())
        );
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        observer.update(user);

        return new AuthResponse(true, "User registered successfully", user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request){
        LoginStrategy strategy = new EmailLoginStrategy(userRepository, encoder);
        User user = strategy.login(request);
        return new AuthResponse(true, "Login successful", user.getName(), user.getEmail());
    }
}
