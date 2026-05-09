package edu.cit.lim.artspire.auth;

import edu.cit.lim.artspire.user.User;

public interface UserObserver {
    void update(User user);
}
