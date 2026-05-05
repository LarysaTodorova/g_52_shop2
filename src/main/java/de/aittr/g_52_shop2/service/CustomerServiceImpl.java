package de.aittr.g_52_shop2.service;

import de.aittr.g_52_shop2.domain.dto.CustomerDto;
import de.aittr.g_52_shop2.domain.entity.Customer;
import de.aittr.g_52_shop2.repository.CustomerRepository;
import de.aittr.g_52_shop2.service.interfaces.CustomerService;
import de.aittr.g_52_shop2.service.mapping.CustomerMappingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMappingService mappingService;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMappingService mappingService) {
        this.repository = repository;
        this.mappingService = mappingService;
    }

    @Override
    public CustomerDto save(CustomerDto dto) {
        Customer entity = mappingService.fromDtoToEntity(dto);
        entity = repository.save(entity);
        if (entity.getCart() != null) {
            entity.getCart().setCustomer(entity);
        }
        return mappingService.fromEntityToDto(entity);
    }

    @Override
    public List<CustomerDto> findAllActiveCustomers() {
        return repository.findAll()
                .stream()
                .filter(Customer::isActive)
                .map(mappingService::fromEntityToDto)
                .toList();
    }

    @Override
    public CustomerDto findById(Long id) {
        Customer customer = repository.findById(id).orElse(null);

        if (customer == null || !customer.isActive()) {
            return null;
        }
        return mappingService.fromEntityToDto(customer);
    }

    @Override
    public void update(CustomerDto customer) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void restoreById(Long id) {

    }

    @Override
    public long getAllActiveCustomersCount() {
        return 0;
    }
}
