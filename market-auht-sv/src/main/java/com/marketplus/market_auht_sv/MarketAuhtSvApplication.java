package com.marketplus.market_auht_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MarketAuhtSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketAuhtSvApplication.class, args);
	}

}
