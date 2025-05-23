package com.marketplus.market_car_sv.repository.apis;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeingClientConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            //verificamos no sea null y que sea de tipo bearer como siempre
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                template.header(HttpHeaders.AUTHORIZATION, jwtToken);
            }
        }
    }
}
