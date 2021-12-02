package com.ideaas.ecomm.ecomm.services.interfaces;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
