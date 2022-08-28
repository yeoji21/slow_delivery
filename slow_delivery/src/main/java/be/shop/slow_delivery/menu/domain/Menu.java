package be.shop.slow_delivery.menu.domain;

import be.shop.slow_delivery.common.domain.BaseTimeEntity;
import be.shop.slow_delivery.common.domain.DisplayInfo;
import be.shop.slow_delivery.shop.domain.Shop;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="menu")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    private String menuName;

    @Column(nullable = true, name = "description")
    private String description;

    @Embedded
    private DisplayInfo displayInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Builder
    public Menu(Shop shop, String menuName, String description, int countMenu){
        this.shop=shop;
        this.menuName=menuName;
        this.description = description;
        this.displayInfo=new DisplayInfo(true,countMenu);
    }

    @Builder
    public Menu(Shop shop,String menuName, String description){
        this.shop=shop;
        this.menuName=menuName;
        this.description = description;
    }

    public void updateMenu(String menuName, String introduction){
        this.menuName=menuName;
        this.description =introduction;
    }

    public void updateMenuDisplay(Boolean isDisplay, int displayOrder){
        DisplayInfo newDisplayInfo = new DisplayInfo(isDisplay,displayOrder);
        this.displayInfo = newDisplayInfo;
    }


}