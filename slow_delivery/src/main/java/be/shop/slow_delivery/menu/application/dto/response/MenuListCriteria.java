package be.shop.slow_delivery.menu.application.dto.response;

import be.shop.slow_delivery.menu.application.dto.MenuListInfo;
import be.shop.slow_delivery.menu.domain.Menu;
import lombok.Data;

import java.util.List;

@Data
public class MenuListCriteria {

    private MenuListInfo menuListInfo;

    public MenuListCriteria(List<Menu> menus){
        menuListInfo = new MenuListInfo(menus);
    }
}