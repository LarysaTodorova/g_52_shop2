package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.dto.UserRegistrationDto;
import de.aittr.g_52_shop2.domain.entity.User;
import de.aittr.g_52_shop2.exception_handling.exceptions.RegistrationException;
import de.aittr.g_52_shop2.repository.UserRepository;
import de.aittr.g_52_shop2.service.interfaces.EmailService;
import de.aittr.g_52_shop2.service.interfaces.RoleService;
import de.aittr.g_52_shop2.service.interfaces.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final EmailService emailService;

    public UserServiceImpl(
            UserRepository repository,
            BCryptPasswordEncoder passwordEncoder,
            RoleService roleService,
            EmailService emailService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.emailService = emailService;
    }

    // При помощи этого метода фреймворк будет получать из БД
    // объекты пользователей вместе с их ролями
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User " + username + " not found")
        );
    }

    @Override
    public void register(UserRegistrationDto registrationDto) {
         /*
        Возможные сценарии регистрации пользователя:
        1. Пользователя ещё нет в БД (он пришёл к нам первый раз).
        2. Не первая попытка регистрации (email в БД есть, confirmed - false).
        3. Попытка регистрации на емейл, который уже подтверждён (email в БД есть, confirmed - true).
         */

        String email = registrationDto.getEmail();
        User user = repository.findByEmail(email).orElse(null);

        if (user == null) {
            // 1 сценарий (частично)
            user = new User();
            user.setEmail(email);
            user.setRoles((Set.of(roleService.getRoleUser())));
            user.setActive(false);
        } else if (user.isActive()) {
            // 3 сценарий
            throw new RegistrationException(String.format("Email %s is already in use", email));
        }
        // общие действия для сценариев 1 и2
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setUsername(registrationDto.getName());

        repository.save(user);

        emailService.sendConfirmationEmail(user);
    }
}
