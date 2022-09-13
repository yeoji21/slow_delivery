package be.shop.slow_delivery.product.domain.validate;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class OptionGroupValidate {
    private long id;
    private String name;
    private Quantity maxSelectCount;
    private List<OptionValidate> options;

    @Builder @QueryProjection
    public OptionGroupValidate(long id,
                               String name,
                               Quantity maxSelectCount,
                               List<OptionValidate> options) {
        this.id = id;
        this.name = name;
        this.maxSelectCount = maxSelectCount;
        this.options = options;
    }

    @Getter @EqualsAndHashCode
    public static class OptionValidate {
        private long id;
        private String name;
        private Money price;

        @Builder @QueryProjection
        public OptionValidate(long id,
                              String name,
                              Money price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }

    public void isSatisfy(OptionGroupValidate validate) {
        Assert.isTrue(id == validate.id, "id");
        Assert.isTrue(name.equals(validate.name), "name");
        Assert.isTrue(maxSelectCount.toInt() >= validate.maxSelectCount.toInt(), "maxSelectCount");
        Assert.isTrue(isEqualList(options, validate.options), "options");
    }

    private <T> boolean isEqualList(final List<T> a, final List<T> b) {
        final Set<T> set = new HashSet<>(a);
        return a.size() == b.size() && set.containsAll(b);
    }
}
