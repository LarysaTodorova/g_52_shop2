package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.entity.ConfirmationCode;
import de.aittr.g_52_shop2.domain.entity.User;
import de.aittr.g_52_shop2.exception_handling.exceptions.RegistrationException;
import de.aittr.g_52_shop2.repository.ConfirmationCodeRepository;
import de.aittr.g_52_shop2.service.interfaces.ConfirmationCodeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmationCodeServiceImpl implements ConfirmationCodeService {

    private final ConfirmationCodeRepository repository;

    public ConfirmationCodeServiceImpl(ConfirmationCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public String generateConfirmationCode(User user) {
        LocalDateTime expiration = LocalDateTime.now().plusHours(24);
        String value = UUID.randomUUID().toString();
        ConfirmationCode entity = new ConfirmationCode(value, expiration, user);
        repository.save(entity);
        return value;
    }

    @Override
    public User getUserByConfirmationCode(String code) {
        ConfirmationCode entity = repository.findByValue(code).orElseThrow(
                () -> new RegistrationException("Confirmation code not found")
        );
        return entity.getUser();
    }
}
