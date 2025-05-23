package com.marketplus.market_auht_sv.service;

import com.marketplus.market_auht_sv.dto.AuthLoginReqDto;
import com.marketplus.market_auht_sv.dto.AuthLoginResDto;
import com.marketplus.market_auht_sv.model.UserSec;
import com.marketplus.market_auht_sv.repository.IUserSecRepository;
import com.marketplus.market_auht_sv.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserSecRepository iUserRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //traer le usu de la db
        UserSec userSec = iUserRepo.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no registrado"));

        //lista que contine todas las authoridades del usu
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //extraermos los roles del usu y los converitmos a SimGranted
        userSec.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName()))));

        //extraemos los permisos, los convertimos a simGranted y los guardamoos en la lista
        userSec.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        //retornmos el usu con el formato conpatible de spring secuirty
        return new User(
                userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isAccountNotLocked(),
                userSec.isCredentialNotExpired(),
                authorityList);

    }

    public Object loginUser(AuthLoginReqDto authLoginReqDto) {

        //recuperamos el user y la password
        String username = authLoginReqDto.username(), password = authLoginReqDto.password();

        Authentication authentication = this.authenticate(username, password);

        //si todo sale bien setieamos el contexto actual del usuario para que sepa que ya esta logeado
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthLoginResDto(username, "login ok", jwtUtils.createToken(authentication), true);

    }

    private Authentication authenticate(String username, String password) {

        //cargar el userDetails service
        UserDetails userDetails = this.loadUserByUsername(username);

        if(!userDetails.getUsername().equals(username)) {
            throw new BadCredentialsException("invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }
}
