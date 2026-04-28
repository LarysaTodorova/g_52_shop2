package de.aittr.g_52_shop2.service.mapping;

import de.aittr.g_52_shop2.domain.dto.CartDto;
import de.aittr.g_52_shop2.domain.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMappingService.class)
public interface CartMappingService {

    CartDto fromEntityToDto(Cart entity);

    Cart fromDtoToEntity(CartDto dto);

}
