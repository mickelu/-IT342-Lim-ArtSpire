package edu.cit.lim.artspire.auth.strategy;

import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.user.User;

public interface LoginStrategy {
    User login(LoginRequest request);
}
