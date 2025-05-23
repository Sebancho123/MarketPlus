package com.marketplus.market_user_sv.service;

import com.marketplus.market_user_sv.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {

    public List<Permission> getAll();
    public Permission save(Permission permission);
    public void deleteById(Long id);
    public Optional<Permission> findById(Long id);
    public Permission update(Permission permission);

}
