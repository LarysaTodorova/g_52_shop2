package de.aittr.g_52_shop2.service.mapping;

import de.aittr.g_52_shop2.domain.dto.CustomerDto;
import de.aittr.g_52_shop2.domain.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartMappingService.class)
public interface CustomerMappingService {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    Customer fromDtoToEntity(CustomerDto dto);

    CustomerDto fromEntityToDto(Customer entity);

//    public Customer fromDtoToEntity(CustomerDto dto) {
//        Customer entity = new Customer();
//        entity.setId(dto.getId());
//        entity.setName(dto.getName());
//        entity.setActive(true);
//        return entity;
//    }
//
//    public CustomerDto fromEntityToDto(Customer entity) {
//        CustomerDto dto = new CustomerDto();
//        dto.setId(entity.getId());
//        dto.setName(entity.getName());
//        return dto;
//    }
}
