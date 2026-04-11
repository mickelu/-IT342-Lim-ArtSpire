package edu.cit.lim.artspire.strategy;

import edu.cit.lim.artspire.dto.LoginRequest;
import edu.cit.lim.artspire.model.User;

public interface LoginStrategy {
    User login(LoginRequest request);
}
