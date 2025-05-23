package com.marketplus.market_user_sv.controller;

import com.marketplus.market_user_sv.model.Role;
import com.marketplus.market_user_sv.service.IRoleService;
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
import java.util.Objects;
import java.util.Optional;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService iRolSer;

    @GetMapping("/getAll")
    public ResponseEntity<List<Role>> getAll() {
        return ResponseEntity.ok(iRolSer.getAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody @Valid Role role) {

        try {
            Role newRole = iRolSer.save(role);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("/findById/{id}")
                    .buildAndExpand(newRole.getId())
                    .toUri();

            return ResponseEntity.created(location).body(newRole);
        }catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return this.defaultException();
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            iRolSer.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return this.defaultException();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) {
        Optional<Role> roleEnc = iRolSer.findById(id);
        return roleEnc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid Role role, @PathVariable Long id) {

        if(!id.equals(role.getId())) {
            throw new BadRequestException("los ids no coinciden!");
        }

        try {
            return ResponseEntity.ok(iRolSer.update(role));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return this.defaultException();
        }

    }

    public ResponseEntity<Object> defaultException() {
        return ResponseEntity.internalServerError().body("uy, error inesperado estamos trabajando en ello!");
    }
}
