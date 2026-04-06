package edu.cit.lim.artspire.service;

import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.dto.RegisterRequest;
import edu.cit.lim.artspire.factory.UserFactory;
import edu.cit.lim.artspire.model.User;
import edu.cit.lim.artspire.repository.UserRepository;
import edu.cit.lim.artspire.strategy.LoginStrategy;
import edu.cit.lim.artspire.strategy.EmailLoginStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String register(RegisterRequest request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return "Email already exists";
        }

        User user = UserFactory.createUser(
                request.getName(),
                request.getEmail(),
                encoder.encode(request.getPassword())
        );

        userRepository.save(user);

        return "User registered successfully";
    }

    public String login(LoginRequest request){
        LoginStrategy strategy = new EmailLoginStrategy(userRepository, encoder);
        return strategy.login(request);
    }
}