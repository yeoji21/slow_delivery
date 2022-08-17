package be.shop.slow_delivery.common.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DisplayInfo {
    @Column(name = "is_display")
    private boolean isDisplay;

    @Column(name ="display_order")
    private DisplayOrder displayOrder;

    @Builder
    public DisplayInfo(boolean isDisplay, int displayOrder) {
        this.isDisplay = isDisplay;
        this.displayOrder = new DisplayOrder(displayOrder);
    }

    public DisplayInfo(int displayOrder) {
        this.isDisplay = true;
        this.displayOrder = new DisplayOrder(displayOrder);
    }
}
