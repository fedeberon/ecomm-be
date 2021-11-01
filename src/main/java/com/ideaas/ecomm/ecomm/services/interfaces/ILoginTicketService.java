package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;

public interface ILoginTicketService {
    LoginTicketResponse save(LoginTicketResponse loginTicket);
}
