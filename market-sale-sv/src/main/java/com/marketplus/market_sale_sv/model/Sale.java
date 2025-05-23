package com.marketplus.market_sale_sv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "este campo no puede ser nulo")
    private Long id_user;
    @NotBlank(message = "el nombre del propietario es obligatorio")
    private String name_user;
    @Temporal(TemporalType.DATE)
    @NotNull(message = "la fecha de creacion no pueder ser nula")
    private LocalDate creation_date;
    @Temporal(TemporalType.DATE)
    private LocalDate pay_date;
    @NotBlank(message = "el estado de la venta no puede estar vacio")
    private String status;
    @NotEmpty(message = "la venta debe de tener almenos un producto comprado")
    private List<String> products;

}
