package be.shop.slow_delivery.shop.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DefaultDeliveryFees {
    @OrderBy("orderAmount asc")
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderAmountDeliveryFee> deliveryFees = new ArrayList<>();

    void add(OrderAmountDeliveryFee deliveryFee) {
        deliveryFees.add(deliveryFee);
    }
}
