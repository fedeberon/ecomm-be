package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Bill;
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
import com.ideaas.ecomm.ecomm.services.interfaces.ILoginTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("billing")
@CrossOrigin
public class BillingController {


    @Value("${afip.token}")
    private String token;

    @Value("${afip.sign}")
    private String sign;

    private ILoginTicketService loginTicketService;

    private IAfipService afipService;
    private IBillService billService;
    private ICheckoutService checkoutService;

    @Autowired
    public BillingController(final ILoginTicketService loginTicketService, final IAfipService afipService,
                             final IBillService billService,
                             final ICheckoutService checkoutService) {
        this.loginTicketService = loginTicketService;
        this.afipService = afipService;
        this.billService = billService;
        this.checkoutService = checkoutService;
    }

    @RequestMapping("{CUIT}")
    public ResponseEntity<Person> getByCUIT(@PathVariable String CUIT) {
        //final LoginTicketResponse ticketResponse = afipService.getAuthentication("ws_sr_padron_a5");
        LoginTicketResponse ticketResponse = new LoginTicketResponse();
        ticketResponse.setToken("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiB1bmlxdWVfaWQ9IjQwNDM5NzY3MTAiIGdlbl90aW1lPSIxNjM1ODUxMjY4IiBleHBfdGltZT0iMTYzNTg5NDUyOCIvPgogICAgPG9wZXJhdGlvbiB0eXBlPSJsb2dpbiIgdmFsdWU9ImdyYW50ZWQiPgogICAgICAgIDxsb2dpbiBlbnRpdHk9IjMzNjkzNDUwMjM5IiBzZXJ2aWNlPSJ3c19zcl9wYWRyb25fYTUiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjAyODU2NDA2NjEsIENOPWZlZGViZXJvbiIgYXV0aG1ldGhvZD0iY21zIiByZWdtZXRob2Q9IjIyIj4KICAgICAgICAgICAgPHJlbGF0aW9ucz4KICAgICAgICAgICAgICAgIDxyZWxhdGlvbiBrZXk9IjIwMjg1NjQwNjYxIiByZWx0eXBlPSI0Ii8+CiAgICAgICAgICAgIDwvcmVsYXRpb25zPgogICAgICAgIDwvbG9naW4+CiAgICA8L29wZXJhdGlvbj4KPC9zc28+Cg==");
        ticketResponse.setSign("L+3NtRGf/5AFumKJLMTIi2cQFTFVO0Giz56LzcctiyrCLoUpQVWZ++C/1lfLCrbLputTjqGECN+Aqqhk6XF1Nc56WRbTUbc1DJIlvZtDlp0wkQJkQ5BRoKCE1T4wwRcyqvlV6qvBI2e1SHYRwxgrmk7p2etVk8l+cvKseV7JhJc=");
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
    public ResponseEntity<Bill> create(final @RequestBody BillRequest billRequest) {
        LoginTicketResponse ticketResponse = afipService.get("wsmtxca");
        BillResponse billResponse = billService.createBilling(ticketResponse, billRequest);
        Checkout checkout = checkoutService.changeStateTo(CheckoutState.PAID_OUT, billRequest.getCheckoutId());
        billResponse.setCheckout(checkout);
        Bill bill = billService.save(billResponse);

        return ResponseEntity.ok(bill);
    }

}
