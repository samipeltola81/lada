package fi.turskacreations.ladaapi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import static fi.turskacreations.ladaapi.security.SecurityConstants.EXPIRATION_TIME;
import static fi.turskacreations.ladaapi.security.SecurityConstants.SECRET;

public class JWTGenerator {

    public String generateToken(String userName){

        String token = Jwts.builder()
                .setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return token;
    }

}
