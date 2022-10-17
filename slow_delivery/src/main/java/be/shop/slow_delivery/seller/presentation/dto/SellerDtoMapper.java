package be.shop.slow_delivery.seller.presentation.dto;

import be.shop.slow_delivery.seller.application.dto.CheckEmailValidateCriteria;
import be.shop.slow_delivery.seller.application.dto.EmailValidateCommand;
import be.shop.slow_delivery.seller.application.dto.SellerSignUpCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerDtoMapper {

    SellerSignUpCommand toCommand(SellerSignUpDto sellerSignUpDto);

    EmailValidateCommand toCommand(EmailValidateDto emailValidateDto);

    CheckEmailValidateCriteria toCriteria(CheckEmailValidateDto checkEmailValidateDto);
}
