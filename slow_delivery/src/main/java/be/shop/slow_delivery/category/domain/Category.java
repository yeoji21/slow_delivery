package be.shop.slow_delivery.category.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="catetory")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, name = "name")
    @Enumerated(EnumType.STRING)
    private CategoryType categoryName;

    @Builder
    public Category(CategoryType categoryName) {
        this.categoryName = categoryName;
    }
}
