package be.shop.slow_delivery.menu.presentation.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MenuForm {

    @NotBlank(message = "메뉴명은 필수입니다.")
    private String menuName;

    private String introduction;

    @Builder
    public MenuForm(String menuName,
                         String introduction) {
        this.menuName = menuName;
        this.introduction = introduction;
    }
}
