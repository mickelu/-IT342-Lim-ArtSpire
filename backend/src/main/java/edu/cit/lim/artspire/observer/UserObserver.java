package edu.cit.lim.artspire.observer;

import edu.cit.lim.artspire.model.User;

public interface UserObserver {
    void update(User user);
}