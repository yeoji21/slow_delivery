package be.shop.slow_delivery.product.application.command;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductCommandMapper {
    ProductCommandMapper INSTANCE = Mappers.getMapper(ProductCommandMapper.class);


}
