package be.shop.slow_delivery.product.presentation;

import be.shop.slow_delivery.product.application.ProductCommandService;
import be.shop.slow_delivery.product.application.ProductQueryService;
import be.shop.slow_delivery.product.application.query.ProductDetailInfo;
import be.shop.slow_delivery.product.presentation.dto.ProductCreateDto;
import be.shop.slow_delivery.product.presentation.dto.ProductDtoMapper;
import be.shop.slow_delivery.product.presentation.dto.ProductValidateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductQueryService productQueryService;
    private final ProductCommandService productCommandService;
    private final ProductDtoMapper productDtoMapper;

    @PostMapping("/product")
    public long createProduct(@RequestBody @Valid ProductCreateDto dto) {
        return productCommandService.create(productDtoMapper.toCreateCommand(dto));
    }

    @GetMapping("/product/{productId}")
    public ProductDetailInfo findProductDetailInfo(@PathVariable long productId) {
        return productQueryService.findProductDetailInfo(productId);
    }

    @PostMapping("/product/validate")
    public void validateProductOrder(@RequestBody ProductValidateDto productValidateDto) {
        productQueryService.validateOrder(productDtoMapper.toValidateCommand(productValidateDto));
    }

}
