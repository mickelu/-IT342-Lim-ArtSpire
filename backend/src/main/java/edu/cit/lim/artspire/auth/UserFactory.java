package edu.cit.lim.artspire.auth;

import edu.cit.lim.artspire.user.User;
import java.time.LocalDateTime;

public class UserFactory {

    public static User createUser(String name, String email, String password){
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
