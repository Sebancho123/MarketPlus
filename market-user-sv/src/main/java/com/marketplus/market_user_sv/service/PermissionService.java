package com.marketplus.market_user_sv.service;

import com.marketplus.market_user_sv.model.Permission;
import com.marketplus.market_user_sv.repository.IPermissionRepository;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService{

    @Autowired
    private IPermissionRepository iPerRepo;

    @Override
    public List<Permission> getAll() {
        return iPerRepo.findAll();
    }

    @Override
    public Permission save(Permission permission) {

        if(iPerRepo.existsByPermissionName(permission.getPermissionName())) {
            throw new BadRequestException("El nombre del permiso ya esta en uso");
        }

        return iPerRepo.save(permission);
    }

    @Override
    public void deleteById(Long id) {
        iPerRepo.deleteById(id);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return iPerRepo.findById(id);
    }

    @Override
    public Permission update(Permission permission) {
        return this.save(permission);
    }
}
