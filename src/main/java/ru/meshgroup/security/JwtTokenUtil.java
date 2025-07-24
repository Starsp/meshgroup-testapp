package ru.meshgroup.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.meshgroup.exception.AuthenticationException;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;
    private final HttpServletRequest servletRequest;
    private static final String USER_ID_CLAIM = "USER_ID";
    private SecretKeySpec secretKeySpec;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        secretKeySpec = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
        jwtParser = Jwts.parser()
                .verifyWith(secretKeySpec)
                .build();
    }

    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(USER_ID_CLAIM, userId);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .claims(claims)
                .signWith(secretKeySpec)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return userId.equals(userDetails.getUsername());
    }

    public Long extractUserIdJwt() {
        String header = servletRequest.getHeader("Authorization");
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationException();
        }
        String token = header.replace("Bearer ", "");
        return Long.valueOf(extractClaim(token, claims -> claims.get(USER_ID_CLAIM).toString()));
    }

    public String extractUserId(String token) {
        return extractClaim(token, claims -> claims.get(USER_ID_CLAIM).toString());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return (Claims) jwtParser.parse(token).getPayload();
    }

}
