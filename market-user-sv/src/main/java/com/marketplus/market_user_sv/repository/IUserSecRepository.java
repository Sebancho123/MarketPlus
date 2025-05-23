package com.marketplus.market_user_sv.repository;

import com.marketplus.market_user_sv.model.UserSec;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserSecRepository extends JpaRepository<UserSec, Long> {

    boolean existsByUsername(String username);
    public Optional<UserSec> findUserEntityByUsername(String username);

}
