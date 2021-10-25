package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.payload.AFIP.FECAE;
import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.payload.AFIP.Person;

import javax.xml.soap.SOAPMessage;

public interface IBillService {

    Person createPersonRequest(String token,
                               String sign,
                               String cuitRepresentada,
                               String idPersona);

    FECAE createCAERequest(LoginTicketResponse ticketResponse,
                           String CUIT);

    SOAPMessage prepareCAE(LoginTicketResponse response,
                           String CUIT) throws Exception;
}
