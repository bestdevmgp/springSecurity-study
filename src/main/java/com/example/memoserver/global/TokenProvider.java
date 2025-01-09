package com.example.memoserver.global;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TokenProvider {
    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final JwtParser parser;

    public TokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtProperties.getSecret()));
        parser = Jwts.parser().verifyWith(key).build();
    }

    public String createToken(String username, boolean isAccess) {
        Date now = new Date();
        Duration duration = Duration.ofMinutes(jwtProperties.getDuration());
        Date expiration = new Date(now.getTime() + duration.toMillis());

        return Jwts.builder()
                .header().add("type", "JWT").add("alg", "HS256").and()
                .claims()
                .issuer(jwtProperties.getIssuer())
                .issuedAt(now)
                .expiration(expiration)
                .subject(username)
                .add("type", isAccess ? TYPE_ACCESS : TYPE_REFRESH)
                .and()
            .signWith(key, Jwts.SIG.HS256)
            .compact();
    }

    public Claims getClaimsFromToken(String token) {
        Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
        return jwsClaims.getPayload();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaimsFromToken(token);

        String type = claims.get("type", String.class);
        if (type == null || !type.equals(TYPE_ACCESS)) throw new IllegalArgumentException("Invalid token");

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("user"));

        UserDetails userDetails = User
                .withUsername(claims.getSubject())
                .password("")
                .authorities(authorities)
                .build();
        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }
}
