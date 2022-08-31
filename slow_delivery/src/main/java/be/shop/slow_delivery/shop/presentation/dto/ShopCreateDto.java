package be.shop.slow_delivery.shop.presentation.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
    가게 생성 DTO
    가게명, 최소 주문 금액, 전화 번호, 가게 주소, 카테고리명,
    소개글, 영업 시간, 휴무일

    대표 이미지는 어떻게 저장할지?
 */
@Getter
@NoArgsConstructor
public class ShopCreateDto {
    @NotBlank(message = "가게명은 필수입니다.")
    private String shopName;
    @NotNull(message = "최소 주문 가격은 필수입니다.")
    private int minOrderAmount;
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phoneNumber;
    @NotBlank(message = "가게 주소는 필수입니다.")
    private String streetAddress;
    @NotBlank(message = "가게 카테고리는 필수입니다.")
    private String category;
    private String introduction;
    private String openingHours;
    private String dayOff;

    @Builder
    public ShopCreateDto(String shopName,
                         int minOrderAmount,
                         String introduction,
                         String phoneNumber,
                         String openingHours,
                         String dayOff,
                         String streetAddress,
                         String category) {
        this.shopName = shopName;
        this.minOrderAmount = minOrderAmount;
        this.introduction = introduction;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.dayOff = dayOff;
        this.streetAddress = streetAddress;
        this.category = category;
    }
}
