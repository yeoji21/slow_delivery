package be.shop.slow_delivery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class AsyncConfig {
    // TODO: 2022/10/01 thread pool 설정 추가 예정
}
