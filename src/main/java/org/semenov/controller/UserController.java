package org.semenov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.semenov.model.User;
import org.semenov.model.enums.Role;
import org.semenov.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model) {
        try {
            model.addAttribute("users", userService.findAll());
            return "userList";
        } catch (Exception e) {
            log.error("Error occurred while retrieving user list: {}", e.getMessage());
            throw e;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        try {
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "userEdit";
        } catch (Exception e) {
            log.error("Error occurred while retrieving user edit form: {}", e.getMessage());
            throw e;
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        try {
            userService.saveUser(user, username, form);
            return "redirect:/user";
        } catch (Exception e) {
            log.error("Error occurred while saving user: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        try {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            return "profile";
        } catch (Exception e) {
            log.error("Error occurred while retrieving user profile: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ) {
        try {
            userService.updateProfile(user, password, email);
            return "redirect:/user/profile";
        } catch (Exception e) {
            log.error("Error occurred while updating user profile: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("subscribe/{user}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        try {
            userService.subscribe(currentUser, user);
            return "redirect:/user-questions/" + user.getId();
        } catch (Exception e) {
            log.error("Error occurred while subscribing to user: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("unsubscribe/{user}")
    public String unsubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user
    ) {
        try {
            userService.unsubscribe(currentUser, user);
            return "redirect:/user-questions/" + user.getId();
        } catch (Exception e) {
            log.error("Error occurred while unsubscribing from user: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("{type}/{user}/list")
    public String userList(
            Model model,
            @PathVariable User user,
            @PathVariable String type
    ) {
        try {
            model.addAttribute("userChannel", user);
            model.addAttribute("type", type);

            if ("subscriptions".equals(type)) {
                model.addAttribute("users", user.getSubscribers());
            } else {
                model.addAttribute("users", user.getSubscribers()); // Возможно, здесь должен быть другой тип данных
            }

            return "subscriptions";
        } catch (Exception e) {
            log.error("Error occurred while retrieving user list: {}", e.getMessage());
            throw e;
        }
    }
}
