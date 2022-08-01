package be.shop.slow_delivery.category.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="catetory")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, name = "category_name")
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}
