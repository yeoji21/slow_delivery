package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SelectCount {
    @Embedded
    @AttributeOverride(name = "quantity", column = @Column(name = "min_select_count", nullable = false))
    private Quantity minCount;
    @Embedded
    @AttributeOverride(name = "quantity", column = @Column(name = "max_select_count", nullable = false))
    private Quantity maxCount;

    public SelectCount(int minCount, int maxCount) {
        Assert.isTrue(maxCount >= minCount, "잘못된 수량입니다.");

        this.minCount = new Quantity(minCount);
        this.maxCount = new Quantity(maxCount);
    }

    public void selectedCountCheck(Quantity quantity) {
        if(minCount.toInt() > quantity.toInt() || quantity.toInt() > maxCount.toInt())
            throw new IllegalArgumentException("invalid ingredients count");
    }
}
