package chukhlantsev.oleg.taskboard.web.security;

import chukhlantsev.oleg.taskboard.domain.exception.AccessDeniedException;
import chukhlantsev.oleg.taskboard.domain.user.Role;
import chukhlantsev.oleg.taskboard.domain.user.User;
import chukhlantsev.oleg.taskboard.service.UserService;
import chukhlantsev.oleg.taskboard.service.props.JwtProperties;
import chukhlantsev.oleg.taskboard.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private  Key key;

    //нужно чтобы ключ создался после автовайринга финальных полей. Поэтому помещаем в @PostConstruct
    //т.е после конструктура ( на этапе которого и произойдет автовайринг финальных полей)
    @PostConstruct
    public void init()
{
    this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
}

public String createAccessToken(Long userID, String username, Set<Role> roles)
{
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("id", userID);
    claims.put("roles", resolveRoles(roles));

    Date now = new Date();
    Date validity = new Date(now.getTime() + jwtProperties.getAccess());

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)
            .compact();
}

    private List<String> resolveRoles(Set<Role> roles) {

        return roles.stream()
                .map(Enum::name)
                .toList();
    }


    public String createRefreshToken(Long userID, String username)
    {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userID);

        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccess());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }

    public JwtResponse refreshUserToken(String refreshToken)
    {
        JwtResponse jwtResponse = new JwtResponse();

        if(!validateToken(refreshToken))
            throw new AccessDeniedException();

        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getByID(userId);

        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(),user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));

        return jwtResponse;
    }

    private String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().get("Id").toString();



    }

    private String getUsername(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token) {

        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        return claims.getBody().getExpiration().after(new Date());

    }

    public Authentication getAuthentication(String token)
    {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }
}
