package edu.cit.lim.artspire.user;

import edu.cit.lim.artspire.auth.dto.RegisterRequest;

public class UserAdapter {

    public static RegisterRequest convertToDto(User user){
        RegisterRequest dto = new RegisterRequest();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
