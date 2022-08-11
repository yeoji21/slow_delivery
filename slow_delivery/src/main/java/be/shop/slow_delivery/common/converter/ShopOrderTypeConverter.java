package be.shop.slow_delivery.common.converter;

import be.shop.slow_delivery.shop.presentation.dto.ShopOrderType;
import org.springframework.core.convert.converter.Converter;


public class ShopOrderTypeConverter implements Converter<String, ShopOrderType> {
    @Override
    public ShopOrderType convert(String source) {
        return ShopOrderType.of(source);
    }
}
