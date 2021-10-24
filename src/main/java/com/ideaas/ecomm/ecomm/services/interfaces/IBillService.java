package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.payload.AFIP.Person;

public interface IBillService {
    Person createPersonRequest(String token,
                               String sign,
                               String cuitRepresentada,
                               String idPersona);
}
