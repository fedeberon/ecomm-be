package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.interfaces.JwtService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JwtServiceImpl implements JwtService {
    public static final String HEADER = "Authorization";
    public static final String PREFIX
            = "Bearer ";
    public static final String AUTHORITIES = "authorities";
    public static final int EXPIRATION_TIME = Integer.MAX_VALUE;
    @Value("${ecomm.api.secret.key}")
    private String SECRET_KEY;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Override
    public String createToken(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> grantedAuthorities = userDetails.getAuthorities();
        String token = Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setId(userDetails.getUsername())
                .claim(AUTHORITIES,
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512,
                        SECRET_KEY.getBytes()).compact();

        return token;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setUpSpringAuthentication(Claims claims) {
        List<String> authorities = (List<String>) claims.get(AUTHORITIES);
        Long id = Long.valueOf((String) claims.get("jti"));

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        auth.setDetails(id);
        SecurityContextHolder.getContext().setAuthentication(auth);

    }

    @Override
    public boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }

    @Override
    public Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    @Override
    public Claims validateToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
    }

    @Override
    public boolean hasRole(Claims claims, String role) {
        return ((List<?>) claims.get(AUTHORITIES)).contains(role);
    }

    @Override
    public Long getUserIdFromToken() throws NotFoundException {
        try {
            return (Long) SecurityContextHolder.getContext().getAuthentication().getDetails();
        } catch (Exception e) {
            LOGGER.error("There was a problem in the incoming request. Exception = [{}].");
            throw new NotFoundException("User is not logged in");
        }
    }

    @Override
    public boolean isRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(role));
    }
}
