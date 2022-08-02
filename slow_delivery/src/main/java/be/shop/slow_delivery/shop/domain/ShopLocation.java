package be.shop.slow_delivery.shop.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class ShopLocation {
    @Column(table = "shop_location", name = "street_address", nullable = false)
    private String streetAddress;

    @Column(name = "local_id", nullable = true)
    private Long localId;

    @Column(table = "shop_location", name = "latitude", nullable = true)
    private Double latitude;

    @Column(table = "shop_location", name = "longitude", nullable = true)
    private Double longitude;

    @Builder
    public ShopLocation(String streetAddress,
                        Long localId,
                        Double latitude,
                        Double longitude) {
        this.streetAddress = streetAddress;
        this.localId = localId;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
