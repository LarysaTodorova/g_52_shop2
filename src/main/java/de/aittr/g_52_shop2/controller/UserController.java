package de.aittr.g_52_shop2.controller;

import de.aittr.g_52_shop2.domain.dto.UserRegistrationDto;
import de.aittr.g_52_shop2.service.interfaces.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestBody UserRegistrationDto registrationDto) {
        service.register(registrationDto);
        return "Registration complete. Please check your email.";
    }
}
