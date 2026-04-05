package de.aittr.g_52_shop2.repository;

import de.aittr.g_52_shop2.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
