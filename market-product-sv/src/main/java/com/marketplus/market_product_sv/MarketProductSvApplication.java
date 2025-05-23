package com.marketplus.market_product_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MarketProductSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketProductSvApplication.class, args);
	}

}
