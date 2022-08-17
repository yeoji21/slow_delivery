package be.shop.slow_delivery.menu.presentation;

import be.shop.slow_delivery.menu.application.MenuService;
import be.shop.slow_delivery.menu.application.dto.request.MenuCreateRequestDto;
import be.shop.slow_delivery.menu.application.dto.request.MenuUpdateRequestDto;
import be.shop.slow_delivery.menu.application.dto.response.MenuListResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;

    @ApiOperation(value = "메뉴 목록 보기")
    @GetMapping("/{shopId}/")
    public MenuListResponseDto getMenuList(@PathVariable("shopId") long shopId){
        return menuService.findShopMenuList(shopId);
    }

    @ApiOperation(value = "메뉴 등록")
    @PostMapping("/{shopId}")
    public long createMenu(@RequestBody @Valid MenuCreateRequestDto menuCreateRequestDto,
                           @PathVariable("shopId") long shopId){
        return menuService.createMenu(menuCreateRequestDto,shopId);
    }

    @ApiOperation(value = "메뉴 수정")
    @PutMapping("/{shopId}/{menuId}")
    public void updateMenu(@RequestBody @Valid MenuUpdateRequestDto menuUpdateRequestDto,
                           @PathVariable("menuId") long menuId){
        menuService.updateMenu(menuId,menuUpdateRequestDto);
    }

    @ApiOperation(value = "메뉴 삭제")
    @DeleteMapping("/{shopId}/{menuId}")
    public void deleteMenu(@PathVariable("menuId") long menuId){
        menuService.deleteMenu(menuId);
    }


}
