package be.shop.slow_delivery.shop.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ShopLocation {
    @Column(table = "shop_location", name = "area_id", nullable = true)
    private Long areaId;
    @Column(table = "shop_location", name = "street_address", nullable = false)
    private String streetAddress;
    @Column(table = "shop_location", name = "latitude", nullable = true)
    private Double latitude;
    @Column(table = "shop_location", name = "longitude", nullable = true)
    private Double longitude;

    @Builder
    public ShopLocation(String streetAddress,
                        Long areaId,
                        Double latitude,
                        Double longitude) {
        this.streetAddress = streetAddress;
        this.areaId = areaId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
