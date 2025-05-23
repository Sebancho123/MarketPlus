package com.marketplus.market_sale_sv.service;

import com.marketplus.market_sale_sv.dto.ProductDto;
import com.marketplus.market_sale_sv.dto.SaleDto;
import com.marketplus.market_sale_sv.model.Sale;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ISaleService {
    public List<Sale> getAll();
    public SaleDto save(Sale sale);
    public SaleDto buy(Set<Long> productsIds);
    public void deleteById(Long id);
    public Optional<Sale> findById(Long id);
    //public SaleDto paid(List<Long> id);
    public List<Sale> viewAllMySales();
    public List<Sale> viewAllMySalesByDate(LocalDate date);
}
