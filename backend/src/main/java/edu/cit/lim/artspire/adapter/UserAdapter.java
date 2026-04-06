package edu.cit.lim.artspire.adapter;

import edu.cit.lim.artspire.model.User;
import edu.cit.lim.artspire.dto.RegisterRequest;

public class UserAdapter {

    public static RegisterRequest convertToDto(User user){
        RegisterRequest dto = new RegisterRequest();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}