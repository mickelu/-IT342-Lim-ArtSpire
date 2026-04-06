package edu.cit.lim.artspire.factory;

import edu.cit.lim.artspire.model.User;
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