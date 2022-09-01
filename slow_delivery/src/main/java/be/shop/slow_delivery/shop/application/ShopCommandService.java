package be.shop.slow_delivery.shop.application;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryRepository;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.shop.application.dto.ShopCommandMapper;
import be.shop.slow_delivery.shop.application.dto.ShopCreateCommand;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static be.shop.slow_delivery.exception.ErrorCode.CATEGORY_NOT_FOUND;
import static be.shop.slow_delivery.exception.ErrorCode.SHOP_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class ShopCommandService {
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final ShopCommandMapper mapper;

    @Transactional
    public long create(ShopCreateCommand createCommand) {
        Category category = categoryRepository.findByCategoryName(createCommand.getCategory())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND));
        Shop newShop = shopRepository.save(mapper.toShop(createCommand, category));
        return newShop.getId();
    }

    @Transactional
    public void toggleOpenStatus(long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));
        shop.toggleOpen();
    }
}
