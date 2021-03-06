package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthenticationResponse implements Serializable {

    private String token;
    private String username;
    private String name;
    private String role;

    public AuthenticationResponse(final String token,
                                  final String username,
                                  final String name,
                                  final String role) {
        this.token = token;
        this.username = username;
        this.name = name;
        this.role = role;
    }

}
