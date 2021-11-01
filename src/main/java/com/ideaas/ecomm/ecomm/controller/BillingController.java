package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("all")
@RestController
@RequestMapping("billing")
public class BillingController {


    @Value("${afip.token}")
    private String token;

    @Value("${afip.sign}")
    private String sign;

    private IAfipService afipService;
    private IBillService billService;

    @Autowired
    public BillingController(IAfipService afipService, IBillService billService) {
        this.afipService = afipService;
        this.billService = billService;
    }

    @RequestMapping("{CUIT}")
    public ResponseEntity<Person> getByCUIT(@PathVariable String CUIT) {
        final LoginTicketResponse ticketResponse = afipService.getAuthentication("ws_sr_padron_a5");
        Person person = billService.createPersonRequest(ticketResponse.getToken(),
                                                        ticketResponse.getSign(),
                                         "20285640661",
                                                        CUIT);

        return ResponseEntity.ok(person);
    }

    @PostMapping("/prepare")
    public ResponseEntity<CAEAResponse> prepareBill(final @RequestBody CAEAResponse caeaResponse) {
        //LoginTicketResponse ticketResponse = afipService.getAuthentication("wsmtxca");
        LoginTicketResponse ticketResponse = new LoginTicketResponse();
        ticketResponse.setToken(token);
        ticketResponse.setSign(sign);
        final CAEAResponse fecae = billService.createCAERequest(ticketResponse, caeaResponse.getCuit());

        return ResponseEntity.ok(fecae);
    }

    @PostMapping("/lastBill")
    public ResponseEntity<LastBillIdResponse> getLastBillId(final @RequestBody LastBillIdResponse lastBillIdRequest) {
        //final LoginTicketResponse ticketResponse = afipService.getAuthentication("wsmtxca");
        LoginTicketResponse ticketResponse = new LoginTicketResponse();
        ticketResponse.setToken(token);
        ticketResponse.setSign(sign);
        LastBillIdResponse lastBillId = billService.getLastBillId(ticketResponse, lastBillIdRequest);
        lastBillId.setBillType(lastBillIdRequest.getBillType());
        lastBillId.setCuit(lastBillIdRequest.getCuit());

        return ResponseEntity.ok(lastBillId);
    }

    @PostMapping
    public ResponseEntity<BillResponse> create(final @RequestBody BillRequest billRequest) {
       //LoginTicketResponse ticketResponse = afipService.getAuthentication("wsmtxca");
        LoginTicketResponse ticketResponse = new LoginTicketResponse();
        ticketResponse.setToken(token);
        ticketResponse.setSign(sign);
        BillResponse billResponse = billService.createBilling(ticketResponse, billRequest);

        return ResponseEntity.ok(billResponse);

    }

}
