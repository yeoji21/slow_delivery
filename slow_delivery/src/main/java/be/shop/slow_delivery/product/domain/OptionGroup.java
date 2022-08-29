package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OptionGroup extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "max_select_count", nullable = false)
    private Quantity maxSelectCount;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionInGroup> options = new ArrayList<>();

    public OptionGroup(String name, Quantity maxSelectCount) {
        Assert.hasText(name, "name");
        Assert.notNull(maxSelectCount, "maxSelectCount");

        this.name = name;
        this.maxSelectCount = maxSelectCount;
    }

    public Money getOptionsAmount(List<Option> options) {
        if(options.size() > maxSelectCount.toInt())
            throw new IllegalArgumentException("invalid option count");
        return options.stream()
                .map(Option::getPrice)
                .reduce(Money.ZERO, Money::add);
    }

    public void addOption(Option option, int displayOrder) {
        options.add(new OptionInGroup(this, option, displayOrder));
    }
}
