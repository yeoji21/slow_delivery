package be.shop.slow_delivery.product.application.command;

import be.shop.slow_delivery.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OptionValidateCommand {
    private long optionId;
    private String optionName;
    private Money optionPrice;

    @Builder
    public OptionValidateCommand(long optionId,
                                 String optionName,
                                 int optionPrice) {
        this.optionId = optionId;
        this.optionName = optionName;
        this.optionPrice = new Money(optionPrice);
    }
}
