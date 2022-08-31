package be.shop.slow_delivery.product.presentation.dto;

import be.shop.slow_delivery.product.application.dto.ProductCreateCommand;
import be.shop.slow_delivery.product.application.dto.ProductPlaceCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    ProductDtoMapper INSTANCE = Mappers.getMapper(ProductDtoMapper.class);

    ProductPlaceCommand toPlaceCommand(ProductPlaceDto productPlaceDto);

    ProductCreateCommand toCreateCommand(ProductCreateDto dto);
}
