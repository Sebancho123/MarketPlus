package com.marketplus.market_auht_sv.repository;

import com.marketplus.market_auht_sv.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserSecRepository extends JpaRepository<UserSec, Long> {

    public Optional<UserSec> findUserEntityByUsername(String username);
    public boolean existsByUsername(String username);
}
