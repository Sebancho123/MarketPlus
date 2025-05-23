package com.marketplus.market_payment_sv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReqDto {
    private String coin;
    private String payMethod;
    private Set<Long> productIds;

}
