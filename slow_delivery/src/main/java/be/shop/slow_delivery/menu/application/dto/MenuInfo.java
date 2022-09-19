package be.shop.slow_delivery.menu.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuInfo {

    private Long menuId;

    private String menuName;

    private String introduction;
}