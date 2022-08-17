package be.shop.slow_delivery.common.domain;

import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DisplayOrder {
    @Column(name = "display_order")
    private int displayOrder;

    public DisplayOrder(int displayOrder) {
        Assert.isTrue(displayOrder >= 0, "잘못된 순서값입니다.");
        this.displayOrder = displayOrder;
    }
}
