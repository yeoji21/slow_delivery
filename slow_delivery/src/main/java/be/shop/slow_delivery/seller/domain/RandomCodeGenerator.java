package be.shop.slow_delivery.seller.domain;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class RandomCodeGenerator {

    public static String getEmailValidateCode(){
        StringBuilder key = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());

        // 인증코드 8자리
        for (int i = 0; i < 8; i++) {
            // 0-2 까지 랜덤
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append(random.nextInt(26) + 97);
                    break;
                case 1:
                    key.append(random.nextInt(26) + 65);
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    break;
            }
        }
        return key.toString();
    }
}
