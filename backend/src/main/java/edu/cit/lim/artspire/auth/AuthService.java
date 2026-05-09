package edu.cit.lim.artspire.auth;

import edu.cit.lim.artspire.auth.dto.AuthResponse;
import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.auth.dto.RegisterRequest;
import edu.cit.lim.artspire.auth.strategy.EmailLoginStrategy;
import edu.cit.lim.artspire.auth.strategy.LoginStrategy;
import edu.cit.lim.artspire.user.User;
import edu.cit.lim.artspire.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
