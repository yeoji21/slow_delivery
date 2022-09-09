package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OptionValidateCommand {
    private long id;
    private String name;
    private Money price;

    @Builder
    public OptionValidateCommand(long id,
                                 String name,
                                 int price) {
        this.id = id;
        this.name = name;
        this.price = new Money(price);
    }
}
