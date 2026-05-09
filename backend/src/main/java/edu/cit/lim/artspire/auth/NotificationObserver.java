package edu.cit.lim.artspire.auth;

import edu.cit.lim.artspire.user.User;

public class NotificationObserver implements UserObserver {

    @Override
    public void update(User user){
        System.out.println("New user registered: " + user.getEmail());
    }
}
