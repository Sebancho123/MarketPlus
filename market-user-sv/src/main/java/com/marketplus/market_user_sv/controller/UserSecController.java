package com.marketplus.market_user_sv.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.marketplus.market_user_sv.model.UserSec;
import com.marketplus.market_user_sv.service.IUserSecService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserSecController {

    @Autowired
    private IUserSecService iUserServ;

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserSec>> getAll() {
        return ResponseEntity.ok(iUserServ.getAll());
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> save(@RequestBody @Valid UserSec userSec) {
        try {
            UserSec newUser = iUserServ.save(userSec);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("/findById/{id}")
                    .buildAndExpand(newUser.getId())
                    .toUri();
            return ResponseEntity.created(location).body(newUser);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return this.defaultException();
        }
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            iUserServ.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (JWTVerificationException | BadRequestException | EntityNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            return this.defaultException();
        }
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserSec> findById(@PathVariable Long id) {
        System.out.println("entro aqui user contro!!");
        Optional<UserSec> userEnc = iUserServ.findById(id);
        return userEnc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> update(@RequestBody @Valid UserSec userSec, @PathVariable Long id) {

        if (!id.equals(userSec.getId())) {
            return ResponseEntity.badRequest().body("los ids no coinciden!");
        }

        try {
            return ResponseEntity.ok(iUserServ.update(userSec));
        } catch (JWTVerificationException | BadRequestException | EntityNotFoundException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            return this.defaultException();
        }
    }

    //@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/findByUsername/{username}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<UserSec> findUserEntityByUsername(@PathVariable String username) {
        System.out.println("entro aqui user contro!!");
        Optional<UserSec> userSec = iUserServ.findUserEntityByUsername(username);
        return userSec.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> defaultException() {
        return ResponseEntity.internalServerError().body("uy, error inesperado estamos trabajando en ello!");
    }
}
