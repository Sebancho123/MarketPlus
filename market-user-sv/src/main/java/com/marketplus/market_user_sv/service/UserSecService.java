package com.marketplus.market_user_sv.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplus.market_user_sv.model.Role;
import com.marketplus.market_user_sv.repository.IRoleRepository;
import com.marketplus.market_user_sv.repository.IUserSecRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import org.authvalid.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.marketplus.market_user_sv.model.UserSec;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserSecService implements IUserSecService {

    @Autowired
    private IUserSecRepository iUserRepo;

    @Autowired
    private IRoleRepository iRolRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public List<UserSec> getAll() {
        return iUserRepo.findAll();
    }

    @Override
    public UserSec save(UserSec userSec) {

        if (iUserRepo.existsByUsername(userSec.getUsername())) {
            throw new BadRequestException("ese username ya esta en uso!");
        }

        try {
            return saveOrUpdate(userSec);
        }catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            authorizeSelfAccess(id);
            iUserRepo.deleteById(id);
        } catch (JWTVerificationException | BadRequestException | EntityNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        return iUserRepo.findById(id);
    }

    @Override
    public UserSec update(UserSec userSec) {
        try {
            authorizeSelfAccess(userSec.getId());
            return saveOrUpdate(userSec);
        } catch (JWTVerificationException | BadRequestException | EntityNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    @Override
    public Optional<UserSec> findUserEntityByUsername(String username) {
        return iUserRepo.findUserEntityByUsername(username);
    }

    public UserSec saveOrUpdate(UserSec userSec) {

        Set<Long> roleIds = userSec.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet());

        List<Role> roles = iRolRepo.findAllById(roleIds);

        if (!roles.isEmpty()) {
            userSec.setPassword(passwordEncoder(userSec.getPassword()));
            userSec.setRoles(new HashSet<>(roles));
            return iUserRepo.save(userSec);
        } else {
            throw new BadRequestException("no hay roles validos");
        }
    }

    public String passwordEncoder(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    public String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //de los atributos creams un HttpServletRequest para traer el token
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                throw new BadRequestException("el token no esta presente o no es de tipo bearer!");
            }

            return jwtToken.substring(7);
        }

        throw new BadRequestException("no podemos acceder a los atributos, solicitud fuera de contexto?");
    }

    public void authorizeSelfAccess(Long idUserToAction) {

        String token = this.getToken();
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        String username = jwtUtils.extractUsername(decodedJWT);

        //ahroa si todo sale bien trermos el usuario sobre el que se quiero actuar
        Optional<UserSec> userSecEnc = this.findById(idUserToAction);

        if (userSecEnc.isEmpty()) {
            throw new EntityNotFoundException("Corrije el id, asegurate que sea el tuyo! pesao");
        }

        UserSec userSecWithGet = userSecEnc.get();

        //verificamos que sea el mismo username que el que quiere hacer alguna accion ya que solo podra hacerla sobre el mismo xd
        if (!username.equals(userSecWithGet.getUsername())) {
            throw new BadRequestException("no se puede manejar este usuario, solo el tuyo, pesao");
        }
    }


}
