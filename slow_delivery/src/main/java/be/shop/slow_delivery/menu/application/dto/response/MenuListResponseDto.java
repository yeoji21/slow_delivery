package be.shop.slow_delivery.menu.application.dto.response;

import be.shop.slow_delivery.menu.application.dto.MenuListDto;
import be.shop.slow_delivery.menu.domain.Menu;
import be.shop.slow_delivery.shop.domain.Shop;
import lombok.Data;

import java.util.List;

@Data
public class MenuListResponseDto {

    private MenuListDto menuListDto;

    public MenuListResponseDto(List<Menu> menus){
        menuListDto = new MenuListDto(menus);
    }
}