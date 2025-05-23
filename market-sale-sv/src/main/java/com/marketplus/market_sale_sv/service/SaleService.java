package com.marketplus.market_sale_sv.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplus.market_sale_sv.dto.ProductDto;
import com.marketplus.market_sale_sv.dto.SaleDto;
import com.marketplus.market_sale_sv.dto.UserSecDto;
import com.marketplus.market_sale_sv.model.Sale;
import com.marketplus.market_sale_sv.repository.ISaleRepository;
import com.marketplus.market_sale_sv.repository.apis.IProdApi;
import com.marketplus.market_sale_sv.repository.apis.IUserApi;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import org.authvalid.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private ISaleRepository iSaleRepo;

    @Autowired
    private IUserApi iUserApi;

    @Autowired
    private IProdApi iProdApi;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public List<Sale> getAll() {
        return iSaleRepo.findAll();
    }

    @Override
    public SaleDto save(Sale sale) {
       return this.saleDtoGenerate(iSaleRepo.save(sale));
    }

    @Override
    public SaleDto buy(Set<Long> productsIds) {
        Sale sale = new Sale();
        UserSecDto user = this.getUserAct();
        sale.setId_user(user.getId());
        sale.setName_user(user.getUsername());
        sale.setCreation_date(LocalDate.now());
        sale.setStatus("YA PAGA");
        sale.setPay_date(null);

        List<ProductDto> products = iProdApi.findAllById(productsIds);
        List<String> productNames = new ArrayList<>();
        if (products.isEmpty()) {
            throw new BadRequestException("esos productos no existen o algo anda mal");
        }
        products.forEach(pro -> productNames.add(pro.getName()));
        sale.setProducts(productNames);
        return this.save(sale);
    }

    @Override
    public void deleteById(Long id) {
        Sale sale = this.findById(id).orElseThrow(() -> new EntityNotFoundException("esta venta no existe!"));
        authorizeSelfAccess(sale.getId_user());
        iSaleRepo.deleteById(id);
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return iSaleRepo.findById(id);
    }

//    @Override
//    public SaleDto paid (List<Long> ids) {
//        List<Sale> sales = iSaleRepo.findAllById(ids);
//
//        for (Sale sale : sales) {
//            sale.setPay_date(LocalDate.now());
//            sale.setStatus("PAGADA");
//        }
//        return this.saleDtoGenerate(iSaleRepo.save(sale));
//    }

    @Override
    public List<Sale> viewAllMySales() {
        UserSecDto userSecDto = this.getUserAct();
        return this.getAll().stream()
                .filter(sale -> sale.getId_user().equals(userSecDto.getId()))
                .toList();
    }

    @Override
    public List<Sale> viewAllMySalesByDate(LocalDate date) {
        UserSecDto userSecDto = this.getUserAct();
        return this.getAll().stream()
                .filter(sale -> sale.getId_user().equals(userSecDto.getId()))
                .filter(sale -> sale.getCreation_date().isAfter(date))
                .toList();
    }

    public String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                throw new BadRequestException("el token no esta presente o no es de tipo correcto");
            }

            return jwtToken.substring(7);
        } else {
            throw new BadRequestException("atributos nulos, peticion fuera de contexto?");
        }
    }

    public void authorizeSelfAccess(Long idToAction) {

        DecodedJWT decodedJWT = jwtUtils.validateToken(this.getToken());
        String username = jwtUtils.extractUsername(decodedJWT);

        UserSecDto user = iUserApi.findById(idToAction).orElseThrow(() -> new EntityNotFoundException("el usuario de la venta no existe, COMO ES ESTO POSIBLE ERROR GRAVE!"));

        if(!username.equals(user.getUsername())) {
            throw new BadRequestException("no tienes acceso para modificar esta venta solo puedes manejar las tuyas!!");
        }
    }

    public UserSecDto getUserAct() {
        DecodedJWT decodedJWT = jwtUtils.validateToken(this.getToken());
        String username = jwtUtils.extractUsername(decodedJWT);
        return iUserApi.findUserEntityByUsername(username).orElseThrow(() -> new EntityNotFoundException("usuario no encontrado"));
    }

    public SaleDto saleDtoGenerate(Sale sale) {
        SaleDto saleDto = new SaleDto();
        saleDto.setStatus(sale.getStatus());
        saleDto.setProducts(sale.getProducts());
        saleDto.setCreation_date(sale.getCreation_date());
        saleDto.setPay_date(sale.getPay_date());
        saleDto.setName_user(sale.getName_user());
        return saleDto;
    }
}
