package com.marketplus.market_user_sv.service;

import com.marketplus.market_user_sv.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {

    public List<Role> getAll();
    public Role save(Role role);
    public void deleteById(Long id);
    public Optional<Role> findById(Long id);
    public Role update(Role role);
}
