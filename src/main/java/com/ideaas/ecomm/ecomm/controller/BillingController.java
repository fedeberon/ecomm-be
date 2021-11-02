package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("billing")
public class BillingController {


    @Value("${afip.token}")
    private String token;

    @Value("${afip.sign}")
    private String sign;

    private IAfipService afipService;
    private IBillService billService;
    private ICheckoutService checkoutService;

    @Autowired
    public BillingController(final IAfipService afipService,
                             final IBillService billService,
                             final ICheckoutService checkoutService) {
        this.afipService = afipService;
        this.billService = billService;
        this.checkoutService = checkoutService;
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
        final LoginTicketResponse ticketResponse = afipService.getAuthentication("wsmtxca");
        /*LoginTicketResponse ticketResponse = new LoginTicketResponse();
        ticketResponse.setToken(token);
        ticketResponse.setSign(sign);*/
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
        Checkout checkout = checkoutService.changeStateTo(CheckoutState.PAID_OUT, billRequest.getCheckoutId());
        billResponse.setCheckout(checkout);

        return ResponseEntity.ok(billResponse);
    }

}
