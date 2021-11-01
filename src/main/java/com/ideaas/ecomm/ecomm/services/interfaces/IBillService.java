package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.BillRequest;
import com.ideaas.ecomm.ecomm.domain.BillResponse;
import com.ideaas.ecomm.ecomm.domain.CAEAResponse;
import com.ideaas.ecomm.ecomm.domain.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.payload.AFIP.Person;

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
