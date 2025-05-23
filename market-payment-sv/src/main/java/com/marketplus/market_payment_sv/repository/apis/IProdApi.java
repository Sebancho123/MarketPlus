package com.marketplus.market_payment_sv.repository.apis;

import com.marketplus.market_payment_sv.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@FeignClient(value = "market-product-sv", configuration = FeingConfig.class)
public interface IProdApi {

    @GetMapping("/product/findAllById/{ids}")
    public List<ProductDto> findAllById(@PathVariable Set<Long> ids);

}
