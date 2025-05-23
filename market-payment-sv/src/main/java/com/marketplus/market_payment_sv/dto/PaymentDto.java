package com.marketplus.market_payment_sv.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {

    private String username;
    private double amount;
    private String coin;
    private String status;
    private LocalDate fecha;
    private String payMethod;
}
