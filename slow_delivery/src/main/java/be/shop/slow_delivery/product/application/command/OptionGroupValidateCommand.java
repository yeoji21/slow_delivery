package be.shop.slow_delivery.product.application.command;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OptionGroupValidateCommand {
    private long id;
    private String name;
    private List<OptionValidateCommand> options;

    @Builder
    public OptionGroupValidateCommand(long id,
                                      String name,
                                      List<OptionValidateCommand> options) {
        this.id = id;
        this.name = name;
        this.options = options;
    }
}
