package be.shop.slow_delivery.product.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.DisplayInfo;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OptionInGroup extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option;

    @Embedded
    private DisplayInfo displayInfo;

    public OptionInGroup(OptionGroup optionGroup, Option option, int displayOrder) {
        Assert.notNull(optionGroup, "optionGroup");
        Assert.notNull(option, "option");
        Assert.notNull(displayOrder, "displayOrder");

        this.optionGroup = optionGroup;
        this.option = option;
        this.displayInfo = new DisplayInfo(displayOrder);
    }
}
