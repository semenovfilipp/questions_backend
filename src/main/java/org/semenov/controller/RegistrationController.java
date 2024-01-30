package org.semenov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.semenov.dto.CaptchaResponseDto;
import org.semenov.model.User;
import org.semenov.service.UserService;
import org.semenov.utils.ControllerUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ) {
        try {
            String url = String.format(CAPTCHA_URL, secret, captchaResponse);
            CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

            if (!response.isSuccess()) {
                model.addAttribute("captchaError", "Fill captcha");
            }

            boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);

            if (isConfirmEmpty) {
                model.addAttribute("password2Error", "Password confirmation cannot be empty");
            }

            if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
                model.addAttribute("passwordError", "Passwords are different!");
            }

            if (isConfirmEmpty || bindingResult.hasErrors() || !response.isSuccess()) {
                Map<String, String> errors = ControllerUtil.getErrors(bindingResult);
                model.mergeAttributes(errors);
                return "registration";
            }

            if (!userService.addUser(user)) {
                model.addAttribute("usernameError", "User exists!");
                return "registration";
            }

            return "redirect:/login";
        } catch (Exception e) {
            log.error("Error occurred while processing registration: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        try {
            boolean isActivated = userService.activateUser(code);

            if (isActivated) {
                model.addAttribute("questionType", "success");
                model.addAttribute("question", "User successfully activated");
            } else {
                model.addAttribute("questionType", "danger");
                model.addAttribute("question", "Activation code is not found!");
            }

            return "login";
        } catch (Exception e) {
            log.error("Error occurred while activating user: {}", e.getMessage());
            throw e;
        }
    }
}
