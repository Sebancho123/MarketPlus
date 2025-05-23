package com.marketplus.market_api_gateway_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MarketApiGatewaySvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketApiGatewaySvApplication.class, args);
	}

}
