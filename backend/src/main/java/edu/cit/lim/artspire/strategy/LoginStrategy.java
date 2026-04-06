package edu.cit.lim.artspire.strategy;

import edu.cit.lim.artspire.dto.LoginRequest;

public interface LoginStrategy {
    String login(LoginRequest request);
}