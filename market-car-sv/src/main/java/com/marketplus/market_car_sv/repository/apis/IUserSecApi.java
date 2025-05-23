package com.marketplus.market_car_sv.repository.apis;

import com.marketplus.market_car_sv.dto.UserSecDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "market-user-sv", configuration = FeingClientConfig.class)
public interface IUserSecApi {

    @GetMapping("/user/findById/{id}")
    public Optional<UserSecDto> findById(@PathVariable Long id);

    @GetMapping("/user/findByUsername/{username}")
    public Optional<UserSecDto> findUserEntityByUsername(@PathVariable String username);
}
