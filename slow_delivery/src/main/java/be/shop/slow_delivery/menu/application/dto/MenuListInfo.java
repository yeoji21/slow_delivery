package be.shop.slow_delivery.menu.application.dto;

import be.shop.slow_delivery.menu.domain.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuListInfo {

    private List<MenuInfo> menuInfoList;

    public MenuListInfo(List<Menu> menuList){

        menuInfoList = new ArrayList<>(menuList.size());

        for(Menu m : menuList){
            menuInfoList.add(new MenuInfo(m.getId(),m.getMenuName(),m.getDescription()));
        }

    }
}