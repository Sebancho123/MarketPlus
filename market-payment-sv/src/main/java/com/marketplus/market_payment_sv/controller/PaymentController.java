package com.marketplus.market_payment_sv.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.marketplus.market_payment_sv.dto.PaymentDto;
import com.marketplus.market_payment_sv.dto.PaymentReqDto;
import com.marketplus.market_payment_sv.model.Payment;
import com.marketplus.market_payment_sv.service.IPaymentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private IPaymentService iPaySer;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> save(@RequestBody PaymentReqDto paymentReqDto) {
        try {
            return ResponseEntity.ok(iPaySer.save(paymentReqDto));
        }catch (EntityNotFoundException | BadRequestException | JWTVerificationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("findById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(iPaySer.findById(id).orElseThrow(() -> new EntityNotFoundException("pago no encontrado!")));
        }catch (BadRequestException | JWTVerificationException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return this.defaultExcep();
        }
    }

    @PutMapping("/confirmPay/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> confirmPayment(@PathVariable Long id, @RequestBody Set<Long> productIds) {
        try {
            return ResponseEntity.ok(iPaySer.confirmPayment(id, productIds));
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
