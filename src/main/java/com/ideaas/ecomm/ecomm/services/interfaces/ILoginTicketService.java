package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;

import java.util.Optional;

public interface ILoginTicketService {
    LoginTicketResponse save(LoginTicketResponse loginTicket);

    Optional<LoginTicketResponse> getActive(String service);
}
