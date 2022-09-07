package be.shop.slow_delivery.product.application.query;

import be.shop.slow_delivery.common.domain.Money;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OptionInfo {
    private long optionId;
    private String name;
    private int price;

    @Builder @QueryProjection
    public OptionInfo(long optionId,
                      String name,
                      Money price) {
        this.optionId = optionId;
        this.name = name;
        this.price = price.toInt();
    }
}
