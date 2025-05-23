package com.marketplus.market_user_sv.service;

import com.marketplus.market_user_sv.model.Permission;
import com.marketplus.market_user_sv.model.Role;
import com.marketplus.market_user_sv.repository.IPermissionRepository;
import com.marketplus.market_user_sv.repository.IRoleRepository;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService{

    @Autowired
    private IRoleRepository iRolRepo;

    @Autowired
    private IPermissionRepository iPerRepo;

    @Override
    public List<Role> getAll() {
        return iRolRepo.findAll();
    }

    @Override
    public Role save(Role role) {

        if(iRolRepo.existsByRoleName(role.getRoleName())) {
            throw new BadRequestException("ese nombre de rol ya existe");
        }

        //sacamos los ids de los permmisions que intenta agregar
        Set<Long> permissionIds = role.getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        List<Permission> permissions = iPerRepo.findAllById(permissionIds);

        if(!permissions.isEmpty()) {
            role.setPermissions(new HashSet<>(permissions));
            return iRolRepo.save(role);
        }
        throw new BadRequestException("no se encontraron permisos validos!");
    }

    @Override
    public void deleteById(Long id) {
        iRolRepo.deleteById(id);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRolRepo.findById(id);
    }

    @Override
    public Role update(Role role) {
        return this.save(role);
    }
}
