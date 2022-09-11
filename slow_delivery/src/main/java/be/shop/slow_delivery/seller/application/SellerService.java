package be.shop.slow_delivery.seller.application;

import be.shop.slow_delivery.seller.application.dto.SellerDto;
import be.shop.slow_delivery.seller.domain.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;

    public void join(SellerDto sellerDto){

    }
}
