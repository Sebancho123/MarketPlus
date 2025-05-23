package com.marketplus.market_payment_sv.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplus.market_payment_sv.dto.PaymentDto;
import com.marketplus.market_payment_sv.dto.PaymentReqDto;
import com.marketplus.market_payment_sv.dto.ProductDto;
import com.marketplus.market_payment_sv.dto.UserSecDto;
import com.marketplus.market_payment_sv.model.Payment;
import com.marketplus.market_payment_sv.repository.IPaymentRepository;
import com.marketplus.market_payment_sv.repository.apis.IProdApi;
import com.marketplus.market_payment_sv.repository.apis.ISaleApi;
import com.marketplus.market_payment_sv.repository.apis.IUserApi;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import org.authvalid.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private IPaymentRepository iPayRepo;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IUserApi iUserApi;

    @Autowired
    private ISaleApi iSaleApi;

    @Autowired
    private IProdApi iProdApi;

    @Override
    public PaymentDto save(PaymentReqDto paymentReqDto) {
        UserSecDto user = this.getUserAct();
        Payment payment = new Payment();
        payment.setId_user(user.getId());
        payment.setStatus("EN PROCESO");
        payment.setCoin(paymentReqDto.getCoin());
        payment.setFecha(LocalDate.now());
        payment.setAmount(this.getAmount(paymentReqDto.getProductIds()));
        payment.setPayMethod(paymentReqDto.getPayMethod());
        iPayRepo.save(payment);
        return this.generatePayDto(payment, user);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        Payment payment = iPayRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("este pago no esta presente!"));
        this.authorizeSelfAccess(payment.getId_user());
        return Optional.of(payment);
    }

    @Override
    public PaymentDto confirmPayment(Long id, Set<Long> productIds) {
        iSaleApi.buy(productIds);
        Payment payment = this.findById(id).orElseThrow(() -> new EntityNotFoundException("payment no encontrada"));
        payment.setStatus("YA PAGA!");
        iPayRepo.save(payment);
        UserSecDto user = this.getUserAct();
        return this.generatePayDto(payment, user);
    }

    @Override
    public double getAmount(Set<Long> productIds) {

        List<ProductDto> products = iProdApi.findAllById(productIds);
        return products.stream()
                .map(ProductDto::getPrice)
                .reduce(0.0, Double::sum);
    }

    public String getToken() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if(jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                throw new BadRequestException("token no presente o no es de tipo bearer");
            }
            return jwtToken.substring(7);
        }else {
            throw new BadRequestException("no hay atributos, solicitud fuera de contextp?");
        }
    }

    public void authorizeSelfAccess(Long idToAct) {
        UserSecDto user = this.getUserAct();
        if(!user.getId().equals(idToAct)) {
            throw new BadRequestException("solo puedes acceder a tus pagos!");
        }
    }

    public UserSecDto getUserAct() {
        DecodedJWT decodedJWT = jwtUtils.validateToken(this.getToken());
        String username = jwtUtils.extractUsername(decodedJWT);
        return iUserApi.findUserEntityByUsername(username).orElseThrow(() -> new EntityNotFoundException("usuario no econtrado, esto es muy extraño"));
    }

    public PaymentDto generatePayDto(Payment payment, UserSecDto user) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setAmount(payment.getAmount());
        paymentDto.setCoin(payment.getCoin());
        paymentDto.setFecha(payment.getFecha());
        paymentDto.setStatus(payment.getStatus());
        paymentDto.setUsername(user.getUsername());
        return paymentDto;
    }

}
