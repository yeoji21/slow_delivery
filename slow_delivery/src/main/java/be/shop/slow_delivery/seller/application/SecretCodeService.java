package be.shop.slow_delivery.seller.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class SecretCodeService {
    private final RedisTemplate<String, String> redisTemplate;

    public String findSighUpCode(String emailAddress) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getSignUpCodeKey(emailAddress)))
                .orElseThrow(IllegalArgumentException::new);
    }

    public void saveSignUpCode(String emailAddress, String secretCode){
        // 이메일로 전송된 코드를 5분간 레디스에 저장
        redisTemplate.opsForValue().set(getSignUpCodeKey(emailAddress), secretCode, 5, TimeUnit.MINUTES);
    }

    private String getSignUpCodeKey(String emailAddress) {
        return "signUp:" + emailAddress;
    }
}
