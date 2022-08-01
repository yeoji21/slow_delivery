package be.shop.slow_delivery.shop.presentation;

import be.shop.slow_delivery.shop.application.ShopQueryService;
import be.shop.slow_delivery.shop.application.dto.ShopSimpleInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ShopController {
    private final ShopQueryService shopQueryService;

    @GetMapping("/shop/{shopId}/simple")
    public ShopSimpleInfo getSimpleInfo(@PathVariable long shopId) {
        return shopQueryService.findSimpleInfo(shopId);
    }

}
