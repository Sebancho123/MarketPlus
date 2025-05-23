package com.marketplus.market_sale_sv.repository.apis;

import com.marketplus.market_sale_sv.dto.UserSecDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(value = "market-user-sv", configuration = FeingConfig.class)
public interface IUserApi {

    @GetMapping("/user/findByUsername/{username}")
    public Optional<UserSecDto> findUserEntityByUsername(@PathVariable String username);

    @GetMapping("/user/findById/{id}")
    public Optional<UserSecDto> findById(@PathVariable Long id);
}
