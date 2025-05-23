package com.marketplus.market_auht_sv.service;

import com.marketplus.market_auht_sv.dto.AuthLoginReqDto;
import com.marketplus.market_auht_sv.model.Role;
import com.marketplus.market_auht_sv.model.UserSec;
import com.marketplus.market_auht_sv.repository.IRoleRepository;
import com.marketplus.market_auht_sv.repository.IUserSecRepository;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSecService implements IUserSecService{

    @Autowired
    private IUserSecRepository iUserRepo;

    @Autowired
    private IRoleRepository iRolRepo;

    @Override
    public AuthLoginReqDto register(UserSec userSec, UserDetailsServiceImp userDetailsServiceImp) {

        if(iUserRepo.existsByUsername(userSec.getUsername())) {
            throw new BadRequestException("el username ya existe!");
        }

        String planeTextPass = userSec.getPassword();
        userSec.setPassword(this.passwordEncoder(planeTextPass));

        //verificamos que los roles existan claro
        Set<Long> rolesIds = userSec.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        List<Role> roles = iRolRepo.findAllById(rolesIds);

        if(!roles.isEmpty()) {
            userSec.setRoles(new HashSet<>(roles));
            iUserRepo.save(userSec);
            return new AuthLoginReqDto(userSec.getUsername(), planeTextPass);
        }else {
            throw new BadRequestException("debes de tener almenos 1 rol valido");
        }

    }

    public String passwordEncoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }


}
