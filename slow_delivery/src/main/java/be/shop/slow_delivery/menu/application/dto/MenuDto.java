package be.shop.slow_delivery.menu.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuDto {

    private Long menuPK;

    private String menuName;

    private String introduction;

    private Boolean isDisplay;

    private Integer displayOrder;
}