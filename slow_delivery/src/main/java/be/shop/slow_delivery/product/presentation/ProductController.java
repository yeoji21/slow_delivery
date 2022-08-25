package be.shop.slow_delivery.product.presentation;

import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.application.dto.ProductDetailInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductQueryService productQueryService;

    @GetMapping("/product/{productId}")
    public ProductDetailInfo productDetailInfo(@PathVariable long productId) {
        return productQueryService.findProductDetailInfo(productId);
    }
}
