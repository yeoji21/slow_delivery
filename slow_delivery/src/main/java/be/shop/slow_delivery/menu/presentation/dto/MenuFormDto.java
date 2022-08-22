package be.shop.slow_delivery.menu.presentation.dto;

import be.shop.slow_delivery.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuFormDto {

    @NotBlank(message = "메뉴명은 필수입니다.")
    private String menuName;

    private String introduction;

    @Builder
    public MenuFormDto(String menuName,
                    String introduction) {
        this.menuName = menuName;
        this.introduction = introduction;
    }

    public Menu toEntity(){
        return Menu.builder()
                .menuName(menuName)
                .build();
    }
}
