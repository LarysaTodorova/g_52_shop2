package de.aittr.g_52_shop2.service.interfaces;

import de.aittr.g_52_shop2.domain.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register(UserRegistrationDto registrationDto);

    void confirm(String code);
}
