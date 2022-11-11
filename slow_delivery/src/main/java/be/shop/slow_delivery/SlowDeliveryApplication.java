package be.shop.slow_delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SlowDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlowDeliveryApplication.class, args);
	}

}