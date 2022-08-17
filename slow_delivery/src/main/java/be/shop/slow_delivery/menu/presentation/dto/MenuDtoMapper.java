package be.shop.slow_delivery.menu.presentation.dto;

import be.shop.slow_delivery.menu.application.dto.request.MenuCreateRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MenuDtoMapper {

    MenuDtoMapper INSTANCE = Mappers.getMapper(MenuDtoMapper.class);

    MenuCreateRequestDto toCreateRequestDto(MenuForm menuForm);
}
