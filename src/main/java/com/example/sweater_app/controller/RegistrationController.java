package com.example.sweater_app.controller;


import com.example.sweater_app.constants.ViewName;
import com.example.sweater_app.model.User;
import com.example.sweater_app.model.enums.Role;
import com.example.sweater_app.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Map;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationController {

    private final UserRepository userRepository;

    @GetMapping("/registration")
    public ModelAndView registration(){
        return new ModelAndView(ViewName.REGISTRATION);
    }

    @PostMapping("/registration")
    public ModelAndView addUser(User user, Map<Object,String> model){
        User userFromDB = userRepository.findByUserName(user.getUserName());
        if (userFromDB!= null){
            model.put("message", "User exist");
            return new ModelAndView(ViewName.REGISTRATION);
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return new ModelAndView("redirect:/login");
    }
}
