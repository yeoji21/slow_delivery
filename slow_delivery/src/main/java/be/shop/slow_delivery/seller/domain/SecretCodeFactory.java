package be.shop.slow_delivery.seller.domain;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SecretCodeFactory {
    public String generate() {
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
        return secretCode.toString();
    }
}
