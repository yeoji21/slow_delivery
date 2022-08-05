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
    private int displayOrder;

    @Builder
    public DisplayInfo(boolean isDisplay, int displayOrder) {
        this.isDisplay = isDisplay;
        this.displayOrder = displayOrder;
    }
}
