package be.shop.slow_delivery.menu.application.dto.request;

import be.shop.slow_delivery.common.domain.DisplayOrder;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class MenuDisplayUpdateRequestDto {

    private boolean isDisplay;

    private int displayOrder;

    @Builder
    public MenuDisplayUpdateRequestDto(boolean isDisplay, int displayOrder) {

        this.isDisplay = isDisplay;
        this.displayOrder = displayOrder;
    }

}
