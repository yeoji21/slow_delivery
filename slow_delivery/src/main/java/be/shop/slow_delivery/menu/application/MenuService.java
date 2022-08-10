package be.shop.slow_delivery.menu.application;

import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.menu.application.dto.request.MenuCreateRequestDto;
import be.shop.slow_delivery.menu.application.dto.request.MenuUpdateRequestDto;
import be.shop.slow_delivery.menu.application.dto.response.MenuListResponseDto;
import be.shop.slow_delivery.menu.domain.Menu;
import be.shop.slow_delivery.menu.domain.MenuRepository;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static be.shop.slow_delivery.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;

    @Transactional(readOnly = true)
    public MenuListResponseDto createMenuList(Long shopId){
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));
        return new MenuListResponseDto(shop);
    }

    @Transactional
    public void createMenu(MenuCreateRequestDto menuCreateRequestDto, Shop shop){
        Menu menu;
        menu = makeMenuEntity(shop, menuCreateRequestDto);
        menuRepository.save(menu);
    }

    @Transactional
    public void updateMenu(Long menuId, MenuUpdateRequestDto menuUpdateRequestDto) throws IOException {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(MENU_NOT_FOUND));
        menu.updateMenu(menuUpdateRequestDto.getMenuName(),menuUpdateRequestDto.getIntroduction(),menuUpdateRequestDto.getDisplayInfo());
    }

    @Transactional
    public void deleteMenu(Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(MENU_NOT_FOUND));
        menuRepository.deleteById(menuId);
    }

    private Menu makeMenuEntity(Shop shop, MenuCreateRequestDto menuCreateRequestDto){
        return menuCreateRequestDto.toEntity(shop);
    }
}
