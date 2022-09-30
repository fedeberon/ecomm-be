package com.ideaas.ecomm.ecomm.services.interfaces;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.domain.AFIP.ResponsePerson;
import com.ideaas.ecomm.ecomm.domain.Bill;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.enums.BillType;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;

import java.util.List;

public interface IBillService {

    ResponsePerson createPersonRequest(String token,
                                       String sign,
                                       String cuitRepresentada,
                                       String idPersona);

    LastBillIdResponse getLastBillId(LoginTicketResponse ticketResponse,
                                     LastBillIdResponse lastBillIdResponse,
                                     BillType billType);

    CAEAResponse createCAERequest(LoginTicketResponse ticketResponse,
                                  String CUIT);

    BillResponse createBilling(LoginTicketResponse ticketResponse,
                               BillRequest billRequest);

    Bill save(BillResponse response, Checkout checkout);

    List<Bill> findAll();

    Bill get(Long id);

    List<Bill>  findAllByUser(User user);

    void getBillTypes(LoginTicketResponse ticketResponse);

    List<Bill> search(BillResponse response, String start, String end);
}
