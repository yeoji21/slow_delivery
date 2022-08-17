package be.shop.slow_delivery.menu.presentation;

import be.shop.slow_delivery.menu.application.MenuService;
import be.shop.slow_delivery.menu.application.dto.request.MenuCreateRequestDto;
import be.shop.slow_delivery.menu.application.dto.request.MenuUpdateRequestDto;
import be.shop.slow_delivery.menu.application.dto.response.MenuListResponseDto;
import be.shop.slow_delivery.menu.presentation.dto.MenuDtoMapper;
import be.shop.slow_delivery.menu.presentation.dto.MenuForm;
import be.shop.slow_delivery.shop.presentation.dto.ShopCreateDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MenuController {

    private final MenuService menuService;
    private final MenuDtoMapper mapper;

    @ApiOperation(value = "메뉴 목록 보기")
    @GetMapping("/{shopId}/menus")
    public MenuListResponseDto getMenuList(@PathVariable("shopId") long shopId){
        return menuService.findShopMenuList(shopId);
    }

//    @ApiOperation(value = "메뉴 등록 페이지로")
//    @GetMapping("{shopId}/menu/new")
//    public String createMenuForm(Model model){
//        model.addAttribute("form",new MenuForm());
//        return "{shopId}/menu//createMenuForm";
//    }


    @ApiOperation(value = "메뉴 등록")
    @PostMapping("/{shopId}/menu/new")
    public String createMenu(@RequestBody @Valid MenuForm menuForm,
                           @PathVariable("shopId") long shopId){
        menuService.createMenu(mapper.toCreateRequestDto(menuForm),shopId);
        return "redirect:/{shopId}/menus";
    }

    @ApiOperation(value = "메뉴 수정")
    @PutMapping("/{shopId}/menu/{menuId}")
    public void updateMenu(@RequestBody @Valid MenuUpdateRequestDto menuUpdateRequestDto,
                           @PathVariable("menuId") long menuId){
        menuService.updateMenu(menuId,menuUpdateRequestDto);
    }

    @ApiOperation(value = "메뉴 삭제")
    @DeleteMapping("/{shopId}/menu/{menuId}")
    public void deleteMenu(@PathVariable("menuId") long menuId){
        menuService.deleteMenu(menuId);
    }


}
