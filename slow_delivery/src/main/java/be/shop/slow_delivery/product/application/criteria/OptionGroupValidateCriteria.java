package be.shop.slow_delivery.product.application.criteria;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OptionGroupValidateCriteria {
    private long id;
    private String name;
    private List<OptionValidateCriteria> options;

    @Builder
    public OptionGroupValidateCriteria(long id,
                                       String name,
                                       List<OptionValidateCriteria> options) {
        this.id = id;
        this.name = name;
        this.options = options;
    }
}
