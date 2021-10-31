package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;

public interface IAfipService {
    LoginTicketResponse getAuthentication(final String service);
}
