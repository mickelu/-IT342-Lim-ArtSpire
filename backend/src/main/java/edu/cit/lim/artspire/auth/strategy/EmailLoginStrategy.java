package edu.cit.lim.artspire.auth.strategy;

import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.user.User;
import edu.cit.lim.artspire.user.UserRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class EmailLoginStrategy implements LoginStrategy {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public EmailLoginStrategy(UserRepository userRepository, BCryptPasswordEncoder encoder){
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User login(LoginRequest request){

        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if(user.isEmpty()){
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid credentials");
        }

        if(!encoder.matches(request.getPassword(), user.get().getPassword())){
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid credentials");
        }

        return user.get();
    }
}
