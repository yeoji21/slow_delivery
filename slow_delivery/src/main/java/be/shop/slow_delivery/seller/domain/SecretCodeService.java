package be.shop.slow_delivery.seller.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class SecretCodeService {
    private final RedisTemplate<String, String> redisTemplate;

    public String generateEmailValidateCode(String emailAddress){
        StringBuilder secretCode = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());

        // 인증코드 8자리
        for (int i = 0; i < 8; i++) {
            // 0-2 까지 랜덤
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    secretCode.append(random.nextInt(26) + 97);
                    break;
                case 1:
                    secretCode.append(random.nextInt(26) + 65);
                    break;
                case 2:
                    secretCode.append((random.nextInt(10)));
                    break;
            }
        }

        // 이메일로 전송된 코드를 5분간 레디스에 저장
        redisTemplate.opsForValue().set(emailAddress, secretCode.toString(), 5, TimeUnit.MINUTES);
        return secretCode.toString();
    }
}
