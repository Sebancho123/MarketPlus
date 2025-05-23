package org.authvalid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JwtUtils {

    @Value("${security.jwt.private.key}")
    private String privateKey;

    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(userGenerator)
                    .build();

            return verifier.verify(token); //el  token es valido retonamos el jwt decodificado
        }catch (JWTVerificationException e) {
            throw  new JWTVerificationException("Invalid Token. Not Authorize");
        }

    }

    public String extractUsername(DecodedJWT decodeJWT) {
        return decodeJWT.getSubject();
    }

    public Claim getClaim(String claimName, DecodedJWT decodedJWT) {
        return decodedJWT.getClaim(claimName);
    }

    public Map<String, Claim> getClaims(DecodedJWT decodedJWT) {
        return decodedJWT.getClaims();
    }

}
