package be.shop.slow_delivery.common.client;

import be.shop.slow_delivery.shop.application.dto.ShopDetailInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "beServiceClient", url = "${feign.url.be-service}")
public interface BeServiceClient {

    @PatchMapping(value = "/shop/{shopId}", produces = "application/json", consumes = "application/json")
    void updateShopInfo(@PathVariable("shopId") long shopId, @RequestBody ShopDetailInfo shopDetailInfo);
}
