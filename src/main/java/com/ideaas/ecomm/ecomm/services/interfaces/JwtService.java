package com.ideaas.ecomm.ecomm.services.interfaces;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtService {
    String createToken(UserDetails userDetails);

    @SuppressWarnings("unchecked")
    void setUpSpringAuthentication(Claims claims);

    boolean existsJWTToken(HttpServletRequest request, HttpServletResponse res);

    Claims validateToken(HttpServletRequest request);

    Claims validateToken(String token);

    boolean hasRole(Claims claims, String role);

    Long getUserIdFromToken();

    boolean isRole(String role);
}
