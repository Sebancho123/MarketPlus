package com.marketplus.market_car_sv.service;

import com.marketplus.market_car_sv.dto.CarDto;
import com.marketplus.market_car_sv.model.Car;

import java.util.List;
import java.util.Optional;

public interface ICarService {

    public List<Car> getAll();
    public Car save(CarDto carDto);
    public void deleteById(Long id);
    public Optional<Car> findById(Long id);
    public Car update(CarDto carDto);
    public void deleteOneProdOfCar(Long id_prod);
}
