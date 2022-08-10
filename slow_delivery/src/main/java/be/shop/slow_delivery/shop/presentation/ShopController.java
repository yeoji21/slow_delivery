package be.shop.slow_delivery.shop.presentation;

import be.shop.slow_delivery.shop.application.ShopCommandService;
import be.shop.slow_delivery.shop.application.ShopQueryService;
import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import be.shop.slow_delivery.shop.application.dto.ShopListQueryResult;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import be.shop.slow_delivery.shop.presentation.dto.ShopCreateDto;
import be.shop.slow_delivery.shop.presentation.dto.ShopDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ShopController {
    private final ShopQueryService shopQueryService;
    private final ShopCommandService shopCommandService;
    private final ShopDtoMapper mapper;

    @PostMapping("/shop")
    public long createShop(@RequestBody @Valid ShopCreateDto shopCreateDto) {
        return shopCommandService.create(mapper.toCreateCommand(shopCreateDto));
    }

    @GetMapping("/shop/{shopId}/simple")
    public ShopSimpleInfo getSimpleInfo(@PathVariable long shopId) {
        return shopQueryService.findSimpleInfo(shopId);
    }

    @GetMapping("shop/{shopId}/detail")
    public ShopDetailInfo getDetailInfo(@PathVariable long shopId) {
        return shopQueryService.findDetailInfo(shopId);
    }

    @GetMapping("/category/{categoryId}/shop")
    public ShopListQueryResult getShopListByCategory(@PathVariable long categoryId,
                                                     @RequestParam(required = true) String order,
                                                     @RequestParam(required = false) String cursor,
                                                     @RequestParam(required = false, defaultValue = "10") int size) {
        return shopQueryService.findShopListByCategory(categoryId, order, cursor, size);
    }
}
