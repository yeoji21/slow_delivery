package be.shop.slow_delivery.menu.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MenuDisplayUpdateCommand {

    private boolean isDisplay;

    private int displayOrder;

    @Builder
    public MenuDisplayUpdateCommand(boolean isDisplay, int displayOrder) {

        this.isDisplay = isDisplay;
        this.displayOrder = displayOrder;
    }

}
