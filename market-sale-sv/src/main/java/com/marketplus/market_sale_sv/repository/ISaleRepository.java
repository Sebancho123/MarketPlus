package com.marketplus.market_sale_sv.repository;

import com.marketplus.market_sale_sv.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {
}
