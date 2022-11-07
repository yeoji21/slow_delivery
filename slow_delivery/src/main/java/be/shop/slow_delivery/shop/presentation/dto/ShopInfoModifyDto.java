package be.shop.slow_delivery.shop.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ShopInfoModifyDto {
    private int minOrderPrice;
    private String description;
    private String openingHours;
    private String dayOff;

    @Builder
    public ShopInfoModifyDto(int minOrderPrice,
                             String description,
                             String openingHours,
                             String dayOff) {
        this.minOrderPrice = minOrderPrice;
        this.description = description;
        this.openingHours = openingHours;
        this.dayOff = dayOff;
    }
}
