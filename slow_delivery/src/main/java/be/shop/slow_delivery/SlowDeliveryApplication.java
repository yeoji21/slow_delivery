package be.shop.slow_delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class SlowDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlowDeliveryApplication.class, args);
	}

}
