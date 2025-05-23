package com.marketplus.market_car_sv.repository.apis;

import com.marketplus.market_car_sv.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Set;

@FeignClient(name = "market-product-sv", configuration = FeingClientConfig.class)
public interface IProductApi {

    @GetMapping("/product/findAllById/{ids}")
    public List<ProductDto> findAllById(@PathVariable Set<Long> ids);
}
