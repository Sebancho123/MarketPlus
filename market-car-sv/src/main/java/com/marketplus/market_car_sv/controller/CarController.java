package com.marketplus.market_car_sv.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.marketplus.market_car_sv.dto.CarDto;
import com.marketplus.market_car_sv.model.Car;
import com.marketplus.market_car_sv.service.ICarService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private ICarService iCarSer;

    @GetMapping("/findAll")
    ///@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<Car>> getAll() {
        return ResponseEntity.ok(iCarSer.getAll());
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> save(@RequestBody CarDto carDto) {
        try {
            iCarSer.save(carDto);
            return ResponseEntity.ok().build();
        }catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteById/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            iCarSer.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException | BadRequestException | JWTVerificationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return this.defaultExc();
        }
    }

    @GetMapping("/findById/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Car> findById(@PathVariable Long id) {
        Optional<Car> carEnc = iCarSer.findById(id);
        return carEnc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> update(@RequestBody CarDto carDto) {
        try {
            iCarSer.update(carDto);
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException | JWTVerificationException | BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteOneProdOfCar/{id_prod}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Object> deleteOneProdOfCar(@PathVariable Long id_prod) {
        try {
            iCarSer.deleteOneProdOfCar(id_prod);
            return ResponseEntity.ok().build();
        }catch (EntityNotFoundException | JWTVerificationException | BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<Object> defaultExc() {
        return ResponseEntity.internalServerError().body("uy, error inesperado!");
    }
}
