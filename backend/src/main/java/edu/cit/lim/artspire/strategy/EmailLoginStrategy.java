package edu.cit.lim.artspire.strategy;

import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.model.User;
import edu.cit.lim.artspire.repository.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class EmailLoginStrategy implements LoginStrategy {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public EmailLoginStrategy(UserRepository userRepository, BCryptPasswordEncoder encoder){
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
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