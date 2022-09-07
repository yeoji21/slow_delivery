package be.shop.slow_delivery.product.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionValidateDto {
    private long id;
    private String name;
    private int price;

    @Builder
    public OptionValidateDto(long id,
                             String name,
                             int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
