package com.marketplus.market_api_gateway_sv.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.marketplus.market_api_gateway_sv.utils.ValidateUtil;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtGlobalAuthenticationFilter implements GlobalFilter {

    @Autowired
    private ValidateUtil validateUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //extraer la ServerHhtpReq para acceder a la cabezera y extraer el token para validarlo
        ServerHttpRequest req = exchange.getRequest();
        String jwtToken = req.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        //validamo que no sea null y que sea de tipo bearer
        if(jwtToken == null || !jwtToken.startsWith("Bearer ")) {
            //si da mal tiramos un 401 unauthorize y cerramos
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        //si todo sale bien extraermos el token
        String token = jwtToken.substring(7);
        //validamos que verifique correctamente si no capturamos nuestra excepcion
        try {
            validateUtil.validateToken(token);
        }catch (JWTVerificationException e) {
            throw new BadRequestException(e.getMessage());
        }catch (Exception e) {
            throw new InternalServerErrorException("uy!, ocurrio un error inesperado, no eres tu somos nosotros");
        }

        //aca si todo sale bien ay arriba mandamos el jwtToken ya pasado este filtro al microservicio consultado
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .build();

        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }
}
