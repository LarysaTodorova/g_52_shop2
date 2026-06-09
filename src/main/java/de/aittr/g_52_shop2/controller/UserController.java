package de.aittr.g_52_shop2.controller;

import de.aittr.g_52_shop2.domain.dto.UserRegistrationDto;
import de.aittr.g_52_shop2.service.interfaces.UserService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/confirm/{code}")
    public String confirmRegistration(@PathVariable String code) {
        service.confirm(code);
        return "Registration confirmed.";
    }
}
