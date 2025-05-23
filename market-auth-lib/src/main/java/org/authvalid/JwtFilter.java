package org.authvalid;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NotBlank HttpServletRequest request,
                                    @NotBlank HttpServletResponse response,
                                    @NotBlank FilterChain filterChain) throws ServletException, IOException {

        System.out.println("estamos enrrando en el filter!?");
        //recuepramos el bearer token
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        //verificamso que no sea null y qeu sea de tipo bearer
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {

            try {
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken.substring(7));
                String username = jwtUtils.extractUsername(decodedJWT);
                String authoritiesStr = jwtUtils.getClaim("authorities", decodedJWT).asString();

                //ahora convertir authorities a grantedAuth
                List<SimpleGrantedAuthority> authorities = Arrays.stream(authoritiesStr.split(","))
                        .map(String::trim)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (JWTVerificationException jw) {
                SecurityContextHolder.clearContext();
                throw new JWTVerificationException(jw.getMessage());
            }
        }

        filterChain.doFilter(request, response);

    }
}
