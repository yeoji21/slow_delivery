package be.shop.slow_delivery.product.presentation;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.product.application.ProductCommandService;
import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.application.dto.ProductDetailInfo;
import be.shop.slow_delivery.product.presentation.dto.ProductDtoMapper;
import be.shop.slow_delivery.product.presentation.dto.ProductPlaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;
    private final ProductDtoMapper productDtoMapper;

    @GetMapping("/product/{productId}")
    public ProductDetailInfo productDetailInfo(@PathVariable long productId) {
        return productQueryService.findProductDetailInfo(productId);
    }

    @PostMapping("/product/place")
    public Money placeAnOrder(@RequestBody ProductPlaceDto productPlaceDto) {
        return productCommandService.placeOrder(productDtoMapper.toPlaceCommand(productPlaceDto));
    }

}
