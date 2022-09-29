package be.shop.slow_delivery.menu.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.DisplayInfo;
import be.shop.slow_delivery.product.domain.Product;
import com.mysema.commons.lang.Assert;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MenuProduct extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private DisplayInfo displayInfo;

    @Builder
    public MenuProduct(Menu menu, Product product, int displayOrder) {
        Assert.notNull(menu, "menu");
        Assert.notNull(product, "product");
        Assert.notNull(displayOrder, "displayOrder");

        this.menu = menu;
        this.product = product;
        this.displayInfo=new DisplayInfo(true,displayOrder);
    }
}
