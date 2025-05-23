package com.marketplus.market_user_sv.controller;

import com.marketplus.market_user_sv.model.Permission;
import com.marketplus.market_user_sv.service.IPermissionService;
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
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private IPermissionService iPerSer;

    @GetMapping("/getAll")
    public ResponseEntity<List<Permission>> getAll() {
        return ResponseEntity.ok(iPerSer.getAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody @Valid Permission permission) {

        try {
            Permission permi = iPerSer.save(permission);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .replacePath("/findById/{id}")
                    .buildAndExpand(permi.getId())
                    .toUri();

            return ResponseEntity.created(location).body(permi);
        }catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            iPerSer.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("uy!, error inesperado");
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Permission> findById(@PathVariable Long id) {
        Optional<Permission> permissionEnc = iPerSer.findById(id);
        return permissionEnc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid Permission permission, @PathVariable Long id) {

        if(!id.equals(permission.getId())) {
            return ResponseEntity.badRequest().body("los ids no coinciden!");
        }

        try {
            return ResponseEntity.ok(iPerSer.update(permission));
        }catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("uy!, error inesperado!");
        }
    }

}
