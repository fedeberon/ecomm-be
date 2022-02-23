package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.AuthenticationRequest;
import com.ideaas.ecomm.ecomm.domain.UpdateCredentialsRequest;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.exception.InvalidPasswordOrUsernameException;
import com.ideaas.ecomm.ecomm.payload.AuthenticationResponse;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import com.ideaas.ecomm.ecomm.services.interfaces.JwtService;
import org.apache.catalina.connector.Response;
import org.apache.commons.lang.StringUtils; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.BadCredentialsException;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationController(final IUserService userService,
                                    final JwtService jwtService,
                                    final AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse>createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        
        try{
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
                        
            final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtService.createToken(userDetails);
            User user = userService.get(userDetails.getUsername());
            AuthenticationResponse tokenResponse = new AuthenticationResponse(token,
                                                                              user.getUsername(),
                                                                              user.getName(),
                                                                              StringUtils.join(user.getAuthorities(), ","));
            return ResponseEntity.ok().body(tokenResponse);
            
            } catch (DisabledException e) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } catch (BadCredentialsException e) {
                System.out.println(e); 
                return ResponseEntity.status(HttpStatus.GONE).build();
            }
    }

    private void authenticate(String username, String password) throws DisabledException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new DisabledException("USER_DISABLED", e);
        }   
    } 
    
    

    @PostMapping("/udpateCredentials")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody UpdateCredentialsRequest updateCredentialsRequest) throws Exception {
        
        try{
            User user = userService.get(updateCredentialsRequest.getUsername());
            // Ecriptar password
            user.setPassword(updateCredentialsRequest.getNewPassword());

            return ResponseEntity.ok().body(tokenResponse);
            
            } catch (DisabledException e) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            } catch (BadCredentialsException e) {
                System.out.println(e); 
                return ResponseEntity.status(HttpStatus.GONE).build();
            }
    }





}
