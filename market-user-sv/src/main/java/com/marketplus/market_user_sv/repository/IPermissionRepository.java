package com.marketplus.market_user_sv.repository;

import com.marketplus.market_user_sv.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByPermissionName(String permissionName);
}
