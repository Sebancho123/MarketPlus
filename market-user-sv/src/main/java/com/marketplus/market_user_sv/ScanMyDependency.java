package com.marketplus.market_user_sv;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.authvalid") //el paquete donde esta todo lo de mi dependencia
//esto equivale a hacer un @Bean ya pero es para que escanee y poder usarlo tambien aqui necesito
//poner mi private key y el user generator para que funcione lo de la validacion del token
public class ScanMyDependency {

}
