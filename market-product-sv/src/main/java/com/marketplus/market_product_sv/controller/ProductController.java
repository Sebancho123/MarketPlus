package com.marketplus.market_product_sv.controller;

import com.marketplus.market_product_sv.model.Product;
import com.marketplus.market_product_sv.service.IProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/product")
@PreAuthorize("hasRole('ADMIN')")
public class ProductController {

    @Autowired
    private IProductService iProdSer;

    @GetMapping("/getAll")
    public ResponseEntity<List<Product>> getAll() {
        return ResponseEntity.ok(iProdSer.getAll());
    }

    @PostMapping("/save")
    public ResponseEntity<Product> save(@RequestBody @Valid Product product) {
        Product newProd = iProdSer.save(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/findById/{id}")
                .buildAndExpand(newProd.getId())
                .toUri();
        return ResponseEntity.created(location).body(newProd);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        try {
            iProdSer.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("uy, error inesperado!");
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Optional<Product> prodEnc = iProdSer.findById(id);
        return prodEnc.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/findAllById/{ids}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Product>> findAllById(@PathVariable Set<Long> ids) {
        System.out.println("emntro aqui produc contrp?!");
        return ResponseEntity.ok(iProdSer.findAllById(ids));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@RequestBody @Valid Product product, @PathVariable Long id) {

        if(!id.equals(product.getId())) {
            return ResponseEntity.badRequest().body("los ids no coinciden!");
        }

        try {
            return ResponseEntity.ok(iProdSer.update(product));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }catch (Exception e) {
            return ResponseEntity.internalServerError().body("uy, error inesperado, no eres tu somos nosotros!");
        }
    }

    @GetMapping("/search/{wordCla}")
    public ResponseEntity<List<Product>> search(@PathVariable String wordCla) {
        return ResponseEntity.ok(iProdSer.search(wordCla));
    }

    @GetMapping("/availableCant/{id}/{cantReq}")
    public ResponseEntity<Boolean> AvailableCant(@PathVariable Long id, @PathVariable int cantReq) {
        return ResponseEntity.ok(iProdSer.AvailableCant(id, cantReq));
    }
}
