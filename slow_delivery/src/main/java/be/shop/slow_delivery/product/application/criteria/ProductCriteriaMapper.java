package be.shop.slow_delivery.product.application.criteria;

import be.shop.slow_delivery.common.domain.Quantity;
import be.shop.slow_delivery.product.domain.SelectCount;
import be.shop.slow_delivery.product.domain.validate.IngredientGroupValidate;
import be.shop.slow_delivery.product.domain.validate.OptionGroupValidate;
import be.shop.slow_delivery.product.domain.validate.ProductValidate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductCriteriaMapper {
    ProductCriteriaMapper INSTANCE = Mappers.getMapper(ProductCriteriaMapper.class);

    @Mappings({
            @Mapping(target = "ingredientGroupValidates", source = "ingredientGroups"),
            @Mapping(target = "optionGroupValidates", source = "optionGroups")
    })
    ProductValidate toProductValidate(ProductValidateCriteria criteria);

    @Mapping(target = "IngredientGroupValidate", source = "ingredientGroups", qualifiedByName = "toIngredientGroupValidate")
    List<IngredientGroupValidate> toIngredientValidate(List<IngredientGroupValidateCriteria> ingredientGroups);

    @Mapping(target = "OptionGroupValidate", source = "optionGroups", qualifiedByName = "toOptionGroupValidate")
    List<OptionGroupValidate> toOptionValidate(List<OptionGroupValidateCriteria> optionGroups);

    default OptionGroupValidate toOptionGroupValidate(OptionGroupValidateCriteria criteria) {
        List<OptionGroupValidate.OptionValidate> options = criteria.getOptions()
                .stream()
                .map(this::toOptionValidate)
                .collect(Collectors.toList());

        return OptionGroupValidate.builder()
                .id(criteria.getId())
                .name(criteria.getName())
                .maxSelectCount(new Quantity(options.size()))
                .options(options)
                .build();
    }

    default OptionGroupValidate.OptionValidate toOptionValidate(OptionValidateCriteria criteria) {
        return OptionGroupValidate.OptionValidate
                .builder()
                .id(criteria.getId())
                .name(criteria.getName())
                .price(criteria.getPrice())
                .build();
    }

    default IngredientGroupValidate toIngredientGroupValidate(IngredientGroupValidateCriteria criteria) {
        List<IngredientGroupValidate.IngredientValidate> ingredients = criteria.getIngredients()
                .stream()
                .map(this::toIngredientValidate)
                .collect(Collectors.toList());

        return IngredientGroupValidate.builder()
                .id(criteria.getId())
                .name(criteria.getName())
                .ingredients(ingredients)
                .selectCount(new SelectCount(ingredients.size(), 100))
                .build();
    }
    default IngredientGroupValidate.IngredientValidate toIngredientValidate(IngredientValidateCriteria criteria) {
        return IngredientGroupValidate.IngredientValidate.builder()
                .id(criteria.getId())
                .name(criteria.getName())
                .price(criteria.getPrice())
                .build();
    }

}
