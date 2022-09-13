package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.seller.application.dto.SellerCreate;
import be.shop.slow_delivery.seller.application.dto.SellerPassword;
import be.shop.slow_delivery.seller.domain.Authority;
import be.shop.slow_delivery.seller.domain.Seller;
import be.shop.slow_delivery.seller.domain.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final PasswordEncoder passwordEncoder;

    public void join(SellerCreate sellerCreate){

        Authority authority = new Authority("ROLE_USER");

        Seller seller = new Seller(sellerCreate.getLoginId(), passwordEncoder.encode(sellerCreate.getPassword()),
                sellerCreate.getEmail(), sellerCreate.getPhoneNumber(),sellerCreate.getUsername());

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

    public Optional<Seller> findSellerByEmail(String email){
        return sellerRepository.findByEmail(email);
    }

    public Optional<Seller> findSellerById(String loginId){
        return sellerRepository.findByLoginId(loginId);
    }

    @Transactional
    public void deleteSeller(Seller seller, SellerPassword password){
        if(passwordEncoder.matches(password.getPassword(),seller.getPassword())){
            sellerRepository.delete(seller);
        }
    }

    //구현해야 할 것 : 로그인, 회원정보 수정, 토큰

}
