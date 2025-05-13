package com.coffeandit.transactionbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableFeignClients
@EnableRetry
@EnableRedisRepositories(basePackages = {"package com.coffeandit.transactionbff.redis"})
public class TransactionBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionBffApplication.class, args);
	}

}
