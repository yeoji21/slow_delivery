package be.shop.slow_delivery.stock.domain;

import be.shop.slow_delivery.common.domain.Quantity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Stock {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    @Column(name = "quantity", nullable = false)
    private Quantity quantity;

    public Stock(Quantity quantity) {
        this.quantity = quantity;
    }

    public void addStock(Quantity quantity) {
        this.quantity = this.quantity.plus(quantity);
    }

    public void reduceStock(Quantity quantity) {
        this.quantity = this.quantity.minus(quantity);
    }
}
