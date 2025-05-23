package com.marketplus.market_sale_sv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {
    private String name_user;
    private LocalDate creation_date;
    private LocalDate pay_date;
    private String status;
    private List<String> products;
}
