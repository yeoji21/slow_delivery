package be.shop.slow_delivery.menu.application.dto.request;

import be.shop.slow_delivery.menu.domain.Menu;
import be.shop.slow_delivery.shop.domain.Shop;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MenuUpdateCommand {

    @NotBlank
    private String menuName;

    private String introduction;

    public Menu toEntity(Shop shop){
        return new Menu(shop,menuName,introduction);
    }
}
