package com.onlinepay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * OnlinePay应用主类
 */
@SpringBootApplication
@EnableJpaAuditing
public class OnlinePayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinePayApplication.class, args);
    }

}