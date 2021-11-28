package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.AuthenticationRequest;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.exception.InvalidPasswordOrUsernameException;
import com.ideaas.ecomm.ecomm.payload.AuthenticationResponse;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.JwtService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticationController {

    private IUserService userService;
    private JwtService jwtService;

    @Autowired
    public AuthenticationController(final IUserService userService,
                                    final JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
            String token = jwtService.createToken(userDetails);
            User user = userService.get(userDetails.getUsername());
            AuthenticationResponse tokenResponse = new AuthenticationResponse(token, user.getUsername(), user.getName());

            return ResponseEntity.ok().body(tokenResponse);
        } catch (InvalidPasswordOrUsernameException e) {
            return ResponseEntity.status(Response.SC_UNAUTHORIZED).body(null);
        }
    }
}
