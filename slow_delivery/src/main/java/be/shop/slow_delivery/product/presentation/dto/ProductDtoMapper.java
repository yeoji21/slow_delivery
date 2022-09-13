package be.shop.slow_delivery.product.presentation.dto;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.application.criteria.IngredientValidateCriteria;
import be.shop.slow_delivery.product.application.criteria.OptionValidateCriteria;
import be.shop.slow_delivery.product.application.criteria.ProductValidateCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    ProductDtoMapper INSTANCE = Mappers.getMapper(ProductDtoMapper.class);

    ProductValidateCriteria toValidateCommand(ProductValidateDto productValidateDto);

    ProductCreateCommand toCreateCommand(ProductCreateDto dto);

    default Money toMoney(int price){
        return new Money(price);
    }

    default Quantity toQuantity(int quantity) {
        return new Quantity(quantity);
    }

    default IngredientValidateCriteria toValidateCommand(IngredientValidateDto dto) {
        return IngredientValidateCriteria.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .build();
    }

    default OptionValidateCriteria toValidateCommand(OptionValidateDto dto) {
        return OptionValidateCriteria.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .build();
    }
}
