package com.marketplus.market_sale_sv.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.marketplus.market_sale_sv.dto.SaleDto;
import com.marketplus.market_sale_sv.model.Sale;
import com.marketplus.market_sale_sv.repository.ISaleRepository;
import com.marketplus.market_sale_sv.service.ISaleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private ISaleService iSaleSer;

    @GetMapping("/findAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Sale>> getAll() {
        return ResponseEntity.ok(iSaleSer.getAll());
    }

    @PostMapping("/buy")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> buy(@RequestBody Set<Long> productIds) {
        try {
            return ResponseEntity.ok(iSaleSer.buy(productIds));
        }catch (BadRequestException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            iSaleSer.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (BadRequestException | JWTVerificationException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return this.defaultExcep();
        }
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Sale> findById(@PathVariable Long id) {
        Optional<Sale> saleEnc = iSaleSer.findById(id);
        return saleEnc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

//    @PutMapping("/paid/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
//    public ResponseEntity<Object> paid(@PathVariable Long id) {
//        try {
//            return ResponseEntity.ok(iSaleSer.paid(id));
//        }catch (EntityNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }catch (Exception e) {
//            return this.defaultExcep();
//        }
//    }

    @GetMapping("/viewAllMySales")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> viewAllMySales() {
        try {
            return ResponseEntity.ok(iSaleSer.viewAllMySales());
        }catch (BadRequestException | EntityNotFoundException | JWTVerificationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return this.defaultExcep();
        }
    }

    @GetMapping("/viewAllMySalesByDate")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> viewAllMySalesByDate(@RequestBody LocalDate date) {
        try {
            return ResponseEntity.ok(iSaleSer.viewAllMySalesByDate(date));
        }catch (EntityNotFoundException | BadRequestException | JWTVerificationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return this.defaultExcep();
        }
    }

    private ResponseEntity<Object> defaultExcep() {
        return ResponseEntity.internalServerError().body("uy, error inesperado!");
    }
}
