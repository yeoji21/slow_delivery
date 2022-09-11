package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.seller.application.dto.SellerDto;
import be.shop.slow_delivery.seller.domain.Authority;
import be.shop.slow_delivery.seller.domain.Seller;
import be.shop.slow_delivery.seller.domain.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(SellerDto sellerDto){

        Authority authority = new Authority("ROLE_USER");

        Seller seller = new Seller(sellerDto.getLoginId(), passwordEncoder.encode(sellerDto.getPassword()),
                sellerDto.getEmail(), sellerDto.getPhoneNumber(),sellerDto.getUsername());

        sellerRepository.save(seller);
    }

    public void setTemPassword(String email, String password){
        Seller seller = sellerRepository.findByEmail(email).get();
        seller.changePassword(passwordEncoder.encode(password));
        sellerRepository.save(seller);
    }
    public void changePassword(Seller seller, String password){
        seller.changePassword(passwordEncoder.encode(password));
        sellerRepository.save(seller);
    }
}
