package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;

public interface IBillService {

    Person createPersonRequest(String token,
                               String sign,
                               String cuitRepresentada,
                               String idPersona);

    LastBillIdResponse getLastBillId(LoginTicketResponse ticketResponse,
                                     LastBillIdResponse lastBillIdResponse);

    CAEAResponse createCAERequest(LoginTicketResponse ticketResponse,
                                  String CUIT);

    BillResponse createBilling(LoginTicketResponse ticketResponse,
                               BillRequest billRequest);
}
