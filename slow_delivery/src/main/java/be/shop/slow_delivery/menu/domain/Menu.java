package be.shop.slow_delivery.menu.domain;

import be.shop.slow_delivery.shop.domain.Shop;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="menu")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu{

    @Id
    @GeneratedValue
    @Column(name = "menu_id")
    private Long menuPK;

    @Column(nullable = false, name="menu_name")
    private String menuName;

    @Column(nullable = true, name="introduction")
    private String introduction;

    @Column(name="is_display")
    private Boolean isDisplay;

    @Column(name="display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    public Menu(Shop shop,String menuName, String introduction,Boolean isDisplay,Integer displayOrder){
        this.shop=shop;
        this.menuName=menuName;
        this.introduction=introduction;
        this.isDisplay=isDisplay;
        this.displayOrder=displayOrder;
    }

    public void updateMenu(String menuName,String introduction,Boolean isDisplay,Integer displayOrder){
        this.menuName=menuName;
        this.introduction=introduction;
        this.isDisplay=isDisplay;
        this.displayOrder=displayOrder;
    }

}