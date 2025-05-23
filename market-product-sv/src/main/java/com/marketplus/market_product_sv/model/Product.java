package com.marketplus.market_product_sv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 15, message = "la marca debe estar entre 3 y 15 caracteres")
    @NotBlank(message = "la marca es requerida!")
    private String mark;

    @Size(min = 3,  max = 10, message = "la categoria debe estar entre 3 y 10 caracteres")
    @NotBlank(message = "la categoria es requerida!")
    private String category;

    @Size(min = 15, max = 1000, message = "la descripcion debe estar entre 3 y 1000 caracteres")
    @NotBlank(message = "la descripcion es requerida!")
    private String description;

    @Size(min = 3, max = 30, message = "el nombre del producto debe estar entre 3 y 30 caracteres")
    @NotBlank(message = "el nombre del producto es requerido!")
    private String name;

    private double price;
    private int availableQtt;
}
