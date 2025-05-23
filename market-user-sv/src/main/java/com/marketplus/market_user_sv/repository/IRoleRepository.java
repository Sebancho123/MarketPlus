package com.marketplus.market_user_sv.repository;

import com.marketplus.market_user_sv.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(String roleName);
}
