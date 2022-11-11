package be.shop.slow_delivery.shop.application;

import be.shop.slow_delivery.category.domain.Category;
import be.shop.slow_delivery.category.domain.CategoryRepository;
import be.shop.slow_delivery.common.client.BeServiceClient;
import be.shop.slow_delivery.exception.NotFoundException;
import be.shop.slow_delivery.shop.application.dto.ShopCommandMapper;
import be.shop.slow_delivery.shop.application.dto.ShopCreateCommand;
import be.shop.slow_delivery.shop.application.dto.ShopInfoModifyCommand;
import be.shop.slow_delivery.shop.domain.Shop;
import be.shop.slow_delivery.shop.domain.ShopRepository;
import be.shop.slow_delivery.shop.infra.ShopQueryDao;
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
    private final BeServiceClient serviceClient;
    private final ShopQueryDao shopQueryDao;
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
        serviceClient.updateShopInfo(shopId, shopQueryDao.findDetailInfo(shopId).orElseThrow(IllegalArgumentException::new));
    }

    @Transactional
    public void update(long shopId, ShopInfoModifyCommand command) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new NotFoundException(SHOP_NOT_FOUND));
        shop.update(mapper.toDomain(command));
    }
}
