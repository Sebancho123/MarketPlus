package com.marketplus.market_auht_sv.service;

import com.marketplus.market_auht_sv.dto.AuthLoginReqDto;
import com.marketplus.market_auht_sv.model.UserSec;
import org.apache.catalina.User;

public interface IUserSecService {
    public AuthLoginReqDto register(UserSec userSec, UserDetailsServiceImp userDetailsServiceImp);
}
