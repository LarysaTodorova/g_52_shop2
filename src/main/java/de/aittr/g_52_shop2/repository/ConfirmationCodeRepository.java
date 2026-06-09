package de.aittr.g_52_shop2.repository;

import de.aittr.g_52_shop2.domain.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Long> {

    Optional<ConfirmationCode> findByValue(String code);
}
