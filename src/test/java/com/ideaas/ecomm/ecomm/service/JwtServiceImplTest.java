package com.ideaas.ecomm.ecomm.service;
import com.ideaas.ecomm.ecomm.exception.NotFoundException;
import com.ideaas.ecomm.ecomm.services.JwtServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class JwtServiceImplTest {

    private JwtServiceImpl jwtService;
    private final String SECRET_KEY = "my-secret-key";

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl();
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", SECRET_KEY);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreateToken_ReturnsValidJwt() {
        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User("testuser", "password", authorities);

        String token = jwtService.createToken(userDetails);

        assertNotNull(token);
        assertTrue(token.startsWith("Bearer "));
    }
    @Test
    void testCreateToken_ContainsRoles() {
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );
        UserDetails userDetails = new User("multiRoleUser", "password", authorities);

        String token = jwtService.createToken(userDetails);
        String jwt = token.replace("Bearer ", "");

        Claims claims = jwtService.validateToken(jwt);
        List<String> roles = (List<String>) claims.get("authorities");

        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    void testSetUpSpringAuthentication_SetsContext() {
        Claims claims = mock(Claims.class);
        when(claims.get("authorities")).thenReturn(Arrays.asList("ROLE_USER"));
        when(claims.getSubject()).thenReturn("testuser");
        when(claims.get("jti")).thenReturn("1");

        jwtService.setUpSpringAuthentication(claims);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("testuser", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }


    @Test
    void testExistsJWTToken_WhenHeaderIsValid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        assertTrue(jwtService.existsJWTToken(request, response));
    }

    @Test
    void testExistsJWTToken_WhenHeaderIsInvalid() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        assertFalse(jwtService.existsJWTToken(request, response));
    }
    @Test
    void testExistsJWTToken_WithIncorrectPrefix_ReturnsFalse() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getHeader("Authorization")).thenReturn("Token xyz123");

        assertFalse(jwtService.existsJWTToken(request, response));
    }

    @Test
    void testValidateToken_HttpServletRequest() {
        String jwt = Jwts.builder()
                .setSubject("testuser")
                .setId("1")
                .claim("authorities", Arrays.asList("ROLE_USER"))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);

        Claims claims = jwtService.validateToken(request);

        assertEquals("testuser", claims.getSubject());
    }

    @Test
    void testValidateToken_String() {
        String jwt = Jwts.builder()
                .setSubject("testuser")
                .setId("1")
                .claim("authorities", Arrays.asList("ROLE_USER"))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();

        Claims claims = jwtService.validateToken(jwt);

        assertEquals("testuser", claims.getSubject());
    }
    @Test
    void testValidateToken_InvalidSignature_ThrowsException() {
        String jwt = Jwts.builder()
                .setSubject("testuser")
                .setId("1")
                .claim("authorities", Arrays.asList("ROLE_USER"))
                .signWith(SignatureAlgorithm.HS512, "otra-clave".getBytes()) // <- Clave inválida
                .compact();

        assertThrows(Exception.class, () -> jwtService.validateToken(jwt));
    }

    @Test
    void testHasRole_WhenHasRole_ReturnsTrue() {
        Claims claims = mock(Claims.class);
        when(claims.get("authorities")).thenReturn(Arrays.asList("ROLE_USER"));

        assertTrue(jwtService.hasRole(claims, "ROLE_USER"));
    }

    @Test
    void testHasRole_WhenDoesNotHaveRole_ReturnsFalse() {
        Claims claims = mock(Claims.class);
        when(claims.get("authorities")).thenReturn(Arrays.asList("ROLE_USER"));

        assertFalse(jwtService.hasRole(claims, "ROLE_ADMIN"));
    }

    @Test
    void testGetUserIdFromToken_WhenUserIsLogged() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("user", null, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        auth.setDetails(99L);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Long result = jwtService.getUserIdFromToken();

        assertEquals(99L, result);
    }

    @Test
    void testGetUserIdFromToken_WhenUserIsNotLogged_ThrowsException() {
        assertThrows(NotFoundException.class, () -> jwtService.getUserIdFromToken());
    }

    @Test
    void testIsRole_WhenUserHasRole() {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("user1", null,
                        Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        assertTrue(jwtService.isRole("ROLE_ADMIN"));
        assertFalse(jwtService.isRole("ROLE_USER"));
    }
}
