package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;

public interface IAfipService {
    LoginTicketResponse get(String service);

    LoginTicketResponse getAuthentication(final String service);
}
