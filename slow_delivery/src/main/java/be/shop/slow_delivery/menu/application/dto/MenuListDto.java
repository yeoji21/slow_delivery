package be.shop.slow_delivery.menu.application.dto;

import be.shop.slow_delivery.menu.domain.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuListDto {

    private List<MenuDto> menuDtoList;

    public MenuListDto(List<Menu> menuList){

        menuDtoList = new ArrayList<>(menuList.size());

        for(Menu m : menuList){
            menuDtoList.add(new MenuDto(m.getId(),m.getMenuName(),m.getIntroduction()));
        }

    }
}