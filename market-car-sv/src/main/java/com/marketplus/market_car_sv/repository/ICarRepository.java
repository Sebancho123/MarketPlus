package com.marketplus.market_car_sv.repository;

import com.marketplus.market_car_sv.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Repository
public interface ICarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c WHERE c.id_user = :id_user")
    public Optional<Car> findById_user(@RequestParam("id_user") Long id_user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM car_products WHERE car_id = :car_id AND id = :id", nativeQuery = true)
    public void deleteConnection(@RequestParam("car_id") Long car_id, @RequestParam("id") Long id);
}
