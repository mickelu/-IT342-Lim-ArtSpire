package edu.cit.lim.artspire.observer;

import edu.cit.lim.artspire.model.User;

public class NotificationObserver implements UserObserver {

    @Override
    public void update(User user){
        System.out.println("New user registered: " + user.getEmail());
    }
}