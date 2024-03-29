package org.semenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.semenov.model.User;
import org.semenov.model.enums.Role;
import org.semenov.repository.UserRepository;
import org.semenov.service.MailSenderService;
import org.semenov.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final MailSenderService mailSender;

    private final PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user) {
        try {
            User userFromDb = userRepository.findByUsername(user.getUsername());

            if (userFromDb != null) {
                return false;
            }

            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setActivationCode(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            sendMessage(user);

            return true;
        } catch (Exception e) {
            log.error("Error occurred while adding user: {}", e.getMessage());
            throw e;
        }
    }

    public void sendMessage(User user) {
        try {
            if (!StringUtils.isEmpty(user.getEmail())) {
                String message = String.format(
                        "Hello, %s! \n" +
                                "Welcome to Sweater. Please, visit next link: http://%s/activate/%s",
                        user.getUsername(),
                        hostname,
                        user.getActivationCode()
                );

                mailSender.send(user.getEmail(), "Activation code", message);
            }
        } catch (Exception e) {
            log.error("Error occurred while sending message to user: {}", e.getMessage());
            throw e;
        }
    }

    public boolean activateUser(String code) {
        try {
            User user = userRepository.findByActivationCode(code);

            if (user == null) {
                return false;
            }

            user.setActivationCode(null);

            userRepository.save(user);

            return true;
        } catch (Exception e) {
            log.error("Error occurred while activating user: {}", e.getMessage());
            throw e;
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepository.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);

        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);

        userRepository.save(user);
    }
}
