package com.marketplus.market_payment_sv.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "debes poner el total a pagar de forma automatica fron!")
    private double amount;
    @NotBlank(message = "debes especificar el tipo de modena de pago")
    private String coin;
    @NotBlank(message = "setea un estado del pago!")
    private String status;
    @NotNull(message = "establece la fecha ordeno el pago")
    private LocalDate fecha;
    @NotBlank(message = "establece un metodo de pago!")
    private String payMethod;
    private Long id_user;
}
