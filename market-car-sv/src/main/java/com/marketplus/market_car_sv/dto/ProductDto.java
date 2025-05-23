package com.marketplus.market_car_sv.dto;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ProductDto {
    private Long id;
    private String mark;
    private String category;
    private String description;
    private String name;
    private double price;
    private int availableQtt;
}
