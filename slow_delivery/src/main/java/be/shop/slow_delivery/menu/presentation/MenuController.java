package be.shop.slow_delivery.menu.presentation;

import be.shop.slow_delivery.menu.application.MenuDisplayService;
import be.shop.slow_delivery.menu.application.MenuService;
import be.shop.slow_delivery.menu.application.dto.request.MenuDisplayUpdateCommand;
import be.shop.slow_delivery.menu.application.dto.request.MenuUpdateCommand;
import be.shop.slow_delivery.menu.application.dto.response.MenuListCriteria;
import be.shop.slow_delivery.menu.presentation.dto.MenuDtoMapper;
import be.shop.slow_delivery.menu.presentation.dto.MenuFormDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;
    private final MenuDisplayService menuDisplayService;
    private final MenuDtoMapper mapper;

    @ApiOperation(value = "메뉴 목록 보기")
    @GetMapping("/shop/{shopId}/menus")
    public MenuListCriteria getMenuList(@PathVariable("shopId") long shopId){
        return menuService.findShopMenuList(shopId);
    }

    @ApiOperation(value = "메뉴 등록")
    @PostMapping("/shop/{shopId}/menu/")
    public Long createMenu(@RequestBody @Valid MenuFormDto menuForm,
                           @PathVariable("shopId") long shopId){
        return menuService.createMenu(mapper.toCreateCommand(menuForm),shopId);
    }

    @ApiOperation(value = "메뉴 수정")
    @PutMapping("/shop/{shopId}/menu/{menuId}")
    public void updateMenu(@RequestBody @Valid MenuUpdateCommand menuUpdateCommand,
                           @PathVariable("menuId") long menuId){
        menuService.updateMenu(menuId, menuUpdateCommand);
    }

    @ApiOperation(value = "메뉴 삭제")
    @DeleteMapping("/shop/{shopId}/menu/{menuId}")
    public void deleteMenu(@PathVariable("menuId") long menuId){
        menuService.deleteMenu(menuId);
    }

    @ApiOperation(value = "메뉴 전시정보 수정")
    @PutMapping("/shop/{shopId}/menu/{menuId}/Display")
    public void updateMenu(@RequestBody @Valid MenuDisplayUpdateCommand menuDisplayUpdateCommand,
                           @PathVariable("menuId") long menuId){
        menuDisplayService.updateDisplayInfo(menuId, menuDisplayUpdateCommand);
    }

}
