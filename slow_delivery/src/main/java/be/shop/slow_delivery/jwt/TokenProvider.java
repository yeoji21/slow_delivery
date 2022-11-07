package be.shop.slow_delivery.jwt;

import be.shop.slow_delivery.config.auth.AuthConstraints;
import be.shop.slow_delivery.config.auth.JwtUserDetails;
import be.shop.slow_delivery.seller.domain.Seller;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider{
    private final long tokenValidityInMilliseconds;
    private final Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long tokenValidityInMilliseconds){
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds * 1000;
        this.key= Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims verify(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(Seller seller){
        return Jwts.builder()
                .setSubject(seller.getUsername())
                .claim(AuthConstraints.HEADER_STRING.getValue(), seller.getRole())
                .claim("seller_id", seller.getId())
                .signWith(key, SignatureAlgorithm.ES512)
                .setExpiration(new Date((new Date()).getTime() + this.tokenValidityInMilliseconds))
                .compact();
    }

    public String createToken(Long sellerId, Authentication authentication){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        Date expirationDate = new Date((new Date()).getTime() + this.tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AuthConstraints.HEADER_STRING.getValue(), authorities)
                .claim("seller_id", sellerId)
                .signWith(key, SignatureAlgorithm.ES512)
                .setExpiration(expirationDate)
                .compact();
    }

    public Authentication getAuthentication(String token){
        Claims claims = verify(token);
        Collection<? extends  GrantedAuthority> authorities =
                Arrays.stream(claims.get(AuthConstraints.HEADER_STRING.getValue()).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        JwtUserDetails userDetails = new JwtUserDetails(claims);
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException  e){
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
