package de.aittr.g_52_shop2.service.interfaces;

import de.aittr.g_52_shop2.domain.entity.User;

public interface ConfirmationCodeService {

    String generateConfirmationCode(User user);

    User getUserByConfirmationCode(String code);
}
