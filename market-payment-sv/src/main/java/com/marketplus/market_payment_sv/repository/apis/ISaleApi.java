package com.marketplus.market_payment_sv.repository.apis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(value = "market-sale-sv", configuration = FeingConfig.class)
public interface ISaleApi {

    @PostMapping("/sale/buy")
    public void buy(@RequestBody Set<Long> productIds);
}
