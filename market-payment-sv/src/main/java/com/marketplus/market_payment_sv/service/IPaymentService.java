package com.marketplus.market_payment_sv.service;

import com.marketplus.market_payment_sv.dto.PaymentDto;
import com.marketplus.market_payment_sv.dto.PaymentReqDto;
import com.marketplus.market_payment_sv.model.Payment;

import java.util.Optional;
import java.util.Set;

public interface IPaymentService {

    public PaymentDto save(PaymentReqDto paymentReqDto);
    public Optional<Payment> findById(Long id);
    public PaymentDto confirmPayment(Long id, Set<Long> productIds);
    public double getAmount(Set<Long> productIds);
}
