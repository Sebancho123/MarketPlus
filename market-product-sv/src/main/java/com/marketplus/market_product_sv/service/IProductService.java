package com.marketplus.market_product_sv.service;

import com.marketplus.market_product_sv.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IProductService {
    public List<Product> getAll();
    public Product save(Product product);
    public void deleteById(Long id);
    public Optional<Product> findById(Long id);
    public List<Product> findAllById(Set<Long> ids);
    public Product update(Product product);
    public List<Product> search(String wordCla);
    public boolean AvailableCant(Long id, int cantReq);
}
