package ru.koryakin.diplomproject.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import ru.koryakin.diplomproject.config.RsaKeyProperties;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    JwtEncoder encoder;

    @Autowired
    private final RsaKeyProperties rsaKeyProperties;

    public TokenService(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));
        // создаем полезную нагрузку токена (Payload JWT)
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                // издатель токена
                .issuer("AuthService")
                // время издания токена
                .issuedAt(now)
                // время жизни токена
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                // тема токена
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    public boolean validate(String token) {
        try {
            Algorithm algorithm = Algorithm.RSA256(rsaKeyProperties.rsaPublicKey(), null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e){
            System.out.println("Exception in verifying "+e.toString());
            return false;
        }
    }
}
