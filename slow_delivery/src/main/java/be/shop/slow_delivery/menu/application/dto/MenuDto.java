package be.shop.slow_delivery.menu.application.dto;

import be.shop.slow_delivery.common.domain.DisplayInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuDto {

    private Long menuId;

    private String menuName;

    private String introduction;
}