package be.shop.slow_delivery.menu.application.dto.response;

import be.shop.slow_delivery.menu.application.dto.MenuListDto;
import be.shop.slow_delivery.shop.domain.Shop;
import lombok.Data;

@Data
public class MenuListResponseDto {

    private Long shopId;

    private MenuListDto menuListDto;

    // 수정 필요 shop - menulist 받아오기
//    public MenuListResponseDto(Shop shop){
//        menuListDto = new MenuListDto(shop.getMenuList());
//    }
}