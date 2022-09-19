package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.seller.application.dto.SellerCommand;
import be.shop.slow_delivery.seller.application.dto.SellerPasswordCriteria;
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

    public void join(SellerCommand sellerCommand){

        Authority authority = new Authority("ROLE_USER");

        Seller seller = new Seller(sellerCommand.getLoginId(), passwordEncoder.encode(sellerCommand.getPassword()),
                sellerCommand.getEmail(), sellerCommand.getPhoneNumber(), sellerCommand.getUsername());

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
    public void deleteSeller(Seller seller, SellerPasswordCriteria password){
        if(passwordEncoder.matches(password.getPassword(),seller.getPassword())){
            sellerRepository.delete(seller);
        }
    }

    //구현해야 할 것 : 로그인, 회원정보 수정, 토큰

}
