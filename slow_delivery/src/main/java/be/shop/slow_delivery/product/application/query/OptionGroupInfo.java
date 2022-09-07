package be.shop.slow_delivery.product.application.query;

import be.shop.slow_delivery.common.domain.Quantity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class OptionGroupInfo {
    private long optionGroupId;
    private String name;
    private int maxSelectCount;
    private List<OptionInfo> options;

    @Builder @QueryProjection
    public OptionGroupInfo(long optionGroupId,
                           String name,
                           Quantity maxSelectCount,
                           List<OptionInfo> options) {
        this.optionGroupId = optionGroupId;
        this.name = name;
        this.maxSelectCount = maxSelectCount.toInt();
        this.options = options;
    }
}
