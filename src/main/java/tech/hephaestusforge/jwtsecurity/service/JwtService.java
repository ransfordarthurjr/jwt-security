package tech.hephaestusforge.jwtsecurity.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    // private static final String SECRET_KEY = "743777217A25432A462D4A614E645267556B58703273357538782F413F442847";
    private final ResourceLoader resourceLoader;
    private final KeyUtil keyUtil;

    /*
    privateer Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }
    */

    public String extractUsername(String jwt) {
        return this.extractClaim(jwt, Claims::getSubject);
    }

    public String generateJwt(UserDetails userDetails) {
        return this.generateJwt(new HashMap<>(), userDetails);
    }

    public String generateJwt(Map<String, Object> extraClaims,
                              UserDetails userDetails) {
        RSAPrivateKey privateKey = null;

        try {
            privateKey = keyUtil.readPrivateKey(this.resourceLoader.getResource("classpath:data/keys/jwt.private.pkcs8.key").getFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                //.signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        final String username = extractUsername((jwt));

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        RSAPublicKey publicKey = null;

        try {
            publicKey = keyUtil.readPublicKey(this.resourceLoader.getResource("classpath:data/keys/jwt.public.key").getFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
