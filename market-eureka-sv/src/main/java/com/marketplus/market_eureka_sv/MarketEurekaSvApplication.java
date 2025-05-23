package com.marketplus.market_eureka_sv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MarketEurekaSvApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketEurekaSvApplication.class, args);
	}

}
