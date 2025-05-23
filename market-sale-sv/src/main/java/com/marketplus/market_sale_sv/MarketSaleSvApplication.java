package com.marketplus.market_sale_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class MarketSaleSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketSaleSvApplication.class, args);
	}

}
