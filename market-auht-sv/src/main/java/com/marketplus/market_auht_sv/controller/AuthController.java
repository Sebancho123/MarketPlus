package com.marketplus.market_auht_sv.controller;

import com.marketplus.market_auht_sv.dto.AuthLoginReqDto;
import com.marketplus.market_auht_sv.model.UserSec;
import com.marketplus.market_auht_sv.service.IUserSecService;
import com.marketplus.market_auht_sv.service.UserDetailsServiceImp;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Autowired
    private IUserSecService iUserSecService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthLoginReqDto authLoginReqDto) {
        try {
            return ResponseEntity.ok(this.userDetailsServiceImp.loginUser(authLoginReqDto));
        }catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid UserSec userSec) {

        try {
            AuthLoginReqDto authLoginReqDto = iUserSecService.register(userSec, userDetailsServiceImp);
            return ResponseEntity.ok(this.login(authLoginReqDto));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
