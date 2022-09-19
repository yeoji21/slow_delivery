package be.shop.slow_delivery.menu.application;

import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.menu.application.dto.request.MenuDisplayUpdateCommand;
import be.shop.slow_delivery.menu.domain.Menu;
import be.shop.slow_delivery.menu.domain.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.shop.slow_delivery.exception.ErrorCode.MENU_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MenuDisplayService {

    private final MenuRepository menuRepository;

    @Transactional
    public void updateDisplayInfo(Long menuId, MenuDisplayUpdateCommand menuDisplayUpdateCommand){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundException(MENU_NOT_FOUND));
        menu.updateMenuDisplay(menuDisplayUpdateCommand.isDisplay(), menuDisplayUpdateCommand.getDisplayOrder());
    }

}
