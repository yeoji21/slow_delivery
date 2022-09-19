package be.shop.slow_delivery.menu.application;

import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.menu.application.dto.request.MenuCreateCommand;
import be.shop.slow_delivery.menu.application.dto.request.MenuUpdateCommand;
import be.shop.slow_delivery.menu.application.dto.response.MenuListCriteria;
import be.shop.slow_delivery.menu.domain.Menu;
import be.shop.slow_delivery.menu.domain.MenuRepository;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static be.shop.slow_delivery.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public MenuListCriteria findShopMenuList(Long shopId){
        List<Menu> menus = menuRepository.findAllByShopId(shopId);
        return new MenuListCriteria(menus);
    }

    @Transactional
    public Long createMenu(MenuCreateCommand menuCreateCommand, Long shopId){
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));
        int countMenu = getMenuCount(shop.getId());
        Menu menu = makeMenuEntity(shop, menuCreateCommand,countMenu);
        menuRepository.save(menu);
        return menu.getId();
    }

    @Transactional
    public void updateMenu(Long menuId, MenuUpdateCommand menuUpdateCommand){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(MENU_NOT_FOUND));
        menu.updateMenu(menuUpdateCommand.getMenuName(), menuUpdateCommand.getIntroduction());
    }

    @Transactional
    public void deleteMenu(Long menuId){
        menuRepository.deleteById(menuId);
    }

    private Menu makeMenuEntity(Shop shop, MenuCreateCommand menuCreateCommand, int countMenu){
        return menuCreateCommand.toEntity(shop,countMenu);
    }

    private int getMenuCount(Long shopId){
        List<Menu> menus = menuRepository.findAllByShopId(shopId);
        return menus.size();
    }
}
