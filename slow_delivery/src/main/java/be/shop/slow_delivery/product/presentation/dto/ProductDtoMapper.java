package be.shop.slow_delivery.product.presentation.dto;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.application.command.IngredientValidateCommand;
import be.shop.slow_delivery.product.application.command.OptionValidateCommand;
import be.shop.slow_delivery.product.application.command.ProductCreateCommand;
import be.shop.slow_delivery.product.application.command.ProductValidateCommand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper {
    ProductDtoMapper INSTANCE = Mappers.getMapper(ProductDtoMapper.class);

    ProductValidateCommand toValidateCommand(ProductValidateDto productValidateDto);

    ProductCreateCommand toCreateCommand(ProductCreateDto dto);

    default Money toMoney(int price){
        return new Money(price);
    }

    default Quantity toQuantity(int quantity) {
        return new Quantity(quantity);
    }

    default IngredientValidateCommand toValidateCommand(IngredientValidateDto dto) {
        return IngredientValidateCommand.builder()
                .ingredientId(dto.getId())
                .ingredientName(dto.getName())
                .ingredientPrice(dto.getPrice())
                .build();
    }

    default OptionValidateCommand toValidateCommand(OptionValidateDto dto) {
        return OptionValidateCommand.builder()
                .optionId(dto.getId())
                .optionName(dto.getName())
                .optionPrice(dto.getPrice())
                .build();
    }
}
