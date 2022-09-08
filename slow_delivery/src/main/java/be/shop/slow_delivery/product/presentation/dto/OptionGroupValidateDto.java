package be.shop.slow_delivery.product.presentation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroupValidateDto {
    private long id;
    private String name;
    private List<OptionValidateDto> options;

    @Builder
    public OptionGroupValidateDto(long id,
                                  String name,
                                  List<OptionValidateDto> options) {
        this.id = id;
        this.name = name;
        this.options = options;
    }
}
