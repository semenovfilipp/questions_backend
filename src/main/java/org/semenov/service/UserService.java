package org.semenov.service;

import org.semenov.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
    boolean addUser(User user);
    void sendMessage(User user);
    boolean activateUser(String code);
    List<User> findAll();
    void saveUser(User user, String username, Map<String, String> form);
    void updateProfile(User user, String password, String email);
    void subscribe(User currentUser, User user);
    public void unsubscribe(User currentUser, User user);
}
