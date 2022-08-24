package be.shop.slow_delivery.product.application.dto;

import be.shop.slow_delivery.common.domain.Money;
import be.shop.slow_delivery.common.domain.Quantity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDetailInfo {
    private long productId;
    private String name;
    private String description;
    private int price;
    private int maxOrderQuantity;
    private long thumbnailFileId;
    private List<IngredientGroupInfo> ingredientGroups;
    private List<OptionGroupInfo> optionGroups;

    @Builder @QueryProjection
    public ProductDetailInfo(long productId,
                             String name,
                             String description,
                             Money price,
                             Quantity maxOrderQuantity,
                             long thumbnailFileId) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price.toInt();
        this.maxOrderQuantity = maxOrderQuantity.toInt();
        this.thumbnailFileId = thumbnailFileId;
    }

    public void setIngredientGroups(List<IngredientGroupInfo> ingredientGroups) {
        this.ingredientGroups = ingredientGroups;
    }

    public void setOptionGroups(List<OptionGroupInfo> optionGroups) {
        this.optionGroups = optionGroups;
    }
}
