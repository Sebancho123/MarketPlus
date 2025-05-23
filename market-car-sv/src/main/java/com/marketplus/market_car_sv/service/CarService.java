package com.marketplus.market_car_sv.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.marketplus.market_car_sv.dto.CarDto;
import com.marketplus.market_car_sv.dto.ProductDto;
import com.marketplus.market_car_sv.dto.UserSecDto;
import com.marketplus.market_car_sv.model.Car;
import com.marketplus.market_car_sv.repository.ICarRepository;
import com.marketplus.market_car_sv.repository.apis.IProductApi;
import com.marketplus.market_car_sv.repository.apis.IUserSecApi;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;
import org.authvalid.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarService implements ICarService {

    @Autowired
    private ICarRepository iCarRepo;

    @Autowired
    private JwtUtils jwtUtils;

    //esto son otros servicios que los consumimos
    @Autowired
    private IProductApi iProductApi;

    @Autowired
    private IUserSecApi iUserApi;

    @Override
    public List<Car> getAll() {
        return iCarRepo.findAll();
    }

    @Override
    public Car save(CarDto carDto) {

        //extraemos token para obtener un Deocded para a su vez extraer el username del que eta ahora mismo iniciado session
        UserSecDto userEnc = this.findUserAct();

        //verificamos que no tenga ya carro
        if(iCarRepo.findById_user(userEnc.getId()).isPresent()) {
            throw new BadRequestException("ya tienes un carro!");
        }

        List<ProductDto> productDtos = iProductApi.findAllById(new HashSet<>(carDto.getProductIds()));

        if (!productDtos.isEmpty()) {
            System.out.println("entrmaos here");
            Car car = new Car();
            car.setId_user(userEnc.getId());
            car.setProducts(productDtos);
            return iCarRepo.save(car);
        } else {
            //esto es 00000.1 imporbable que pase
            throw new BadRequestException("no hay productos validos");
        }

    }

    @Override
    public void deleteById(Long id) {
        Car car = this.findById(id).orElseThrow(() -> new EntityNotFoundException("carro no encontrado"));
        authorizeSelfAccess(car.getId_user());
        iCarRepo.deleteById(id);
    }

    @Override
    public Optional<Car> findById(Long id) {
        return iCarRepo.findById(id);
    }

    @Override
    public Car update(CarDto carDto) {

        //obetenemos el carro del user actual y el user actual
        Car car = this.getCarOfUser();
        //verificamos que sea el mismo el que edite el mismo user
        authorizeSelfAccess(car.getId_user());
        List<ProductDto> productDtos = iProductApi.findAllById(new HashSet<>(carDto.getProductIds()));
        if(!productDtos.isEmpty()) {
            car.getProducts().addAll(productDtos);
        }
        return iCarRepo.save(car);
    }

    @Override
    public void deleteOneProdOfCar(Long id_prod) {
        Car car = this.getCarOfUser();
        List<ProductDto> productDtos = new ArrayList<>();
        for (ProductDto p : car.getProducts()) {
            if(!p.getId().equals(id_prod)) {
               productDtos.add(p);
                System.out.println("bubub");
            }
        }
        iCarRepo.deleteConnection(car.getId(), id_prod);
        car.setProducts(productDtos);
        iCarRepo.save(car);
    }

    //para obtener el token act
    public String getToken() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //validmos el el attrributes no se anulo
        if (attributes != null) {
            //ahora creamos el HttpServletReq que necesitamos para extraer el token de la header
            HttpServletRequest request = attributes.getRequest();
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
                throw new BadRequestException("el token no esta presente o no es de tipo bearer!");
            }
            return jwtToken.substring(7);
        } else {
            throw new BadRequestException("no podemos acceder a los atributos, solicitud fuera de contexto?");
        }
    }

    //para ver si el que intenta delete/update es el mismo que el que tiene la incicion sessiada
    public void authorizeSelfAccess(Long idUserToAction) {

        String token = this.getToken();
        DecodedJWT decodedJWT = jwtUtils.validateToken(token);
        String username = jwtUtils.extractUsername(decodedJWT);

        Optional<UserSecDto> userSec = iUserApi.findById(idUserToAction);

        if (userSec.isEmpty()) {
            //esto es muy imposible de acceder amenos que sea un hacker
            throw new BadRequestException("que intentas hacer? este id no existe");
        }

        UserSecDto userGet = userSec.get();

        if (!username.equals(userGet.getUsername())) {
            throw new BadRequestException("solo puedes actuar en tu carrito no el de los demas, pesao");
        }
    }

    //para obtener el carro del user actual
    public Car getCarOfUser() {
        UserSecDto userSecDto = this.findUserAct();
        return iCarRepo.findById_user(userSecDto.getId()).orElseThrow(() -> new EntityNotFoundException("el carro no esta presente!"));
    }

    //para obtener el user actual
    public UserSecDto findUserAct() {
        String token = this.getToken();
        String username = jwtUtils.extractUsername(jwtUtils.validateToken(token));
        //lo traemos para agregar el id del usu que ahora mismo esta en la session
        //para evitar que lo pasen por el body y lo midifiquen
        return iUserApi.findUserEntityByUsername(username).orElseThrow(() -> new EntityNotFoundException("el usu que intentan guardar no existe!"));
    }
}
