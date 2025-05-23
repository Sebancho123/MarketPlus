package com.marketplus.market_product_sv.service;

import com.marketplus.market_product_sv.model.Product;
import com.marketplus.market_product_sv.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepository iProdRepo;

    @Override
    public List<Product> getAll() {
        List<Product> products = iProdRepo.findAll();
        return products.stream()
                .filter(prod -> prod.getAvailableQtt() > 0)
                .toList();
    }

    @Override
    public Product save(Product product) {
        return iProdRepo.save(product);
    }

    @Override
    public void deleteById(Long id) {
        iProdRepo.deleteById(id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return iProdRepo.findById(id);
    }

    @Override
    public List<Product> findAllById(Set<Long> ids) {
        return iProdRepo.findAllById(ids);
    }

    @Override
    public Product update(Product product) {
        return iProdRepo.save(product);
    }

    @Override
    public List<Product> search(String wordCla) {

        List<Product> products = this.getAll();
        return products.stream()
                .filter(p -> p.getCategory().contains(wordCla) || p.getName().contains(wordCla) ||
                        p.getMark().contains(wordCla) || p.getDescription().contains(wordCla))
                .toList();
    }

    @Override
    public boolean AvailableCant(Long id, int cantReq) {
        Optional<Product> product = this.findById(id);

        if(product.isPresent()) {
            Product productGet = product.get();
            return productGet.getAvailableQtt() >= cantReq;
        }

        return false;
    }
}
