package com.marketplus.market_user_sv.service;

import com.marketplus.market_user_sv.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserSecService {

    public List<UserSec> getAll();
    public UserSec save(UserSec userSec);
    public void deleteById(Long id);
    public Optional<UserSec> findById(Long id);
    public UserSec update(UserSec userSec);
    public Optional<UserSec> findUserEntityByUsername(String username);
}
