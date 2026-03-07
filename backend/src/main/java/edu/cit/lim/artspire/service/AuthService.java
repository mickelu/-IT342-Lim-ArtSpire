package edu.cit.lim.artspire.service;

import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.dto.RegisterRequest;
import edu.cit.lim.artspire.model.User;
import edu.cit.lim.artspire.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return "Email already exists";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(LoginRequest request){

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty()){
            return "Invalid credentials";
        }

        if(!encoder.matches(request.getPassword(), user.get().getPassword())){
            return "Invalid credentials";
        }

        return "Login successful";
    }
}