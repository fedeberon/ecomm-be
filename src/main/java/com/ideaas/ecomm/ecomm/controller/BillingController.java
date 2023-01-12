package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.domain.AFIP.Person;
import com.ideaas.ecomm.ecomm.domain.AFIP.ResponsePerson;
import com.ideaas.ecomm.ecomm.domain.Bill;
import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.enums.BillType;
import com.ideaas.ecomm.ecomm.enums.CheckoutState;
import com.ideaas.ecomm.ecomm.payload.BillRequest;
import com.ideaas.ecomm.ecomm.payload.BillResponse;
import com.ideaas.ecomm.ecomm.payload.CAEAResponse;
import com.ideaas.ecomm.ecomm.payload.LastBillIdResponse;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import com.ideaas.ecomm.ecomm.services.interfaces.ICheckoutService;
import com.ideaas.ecomm.ecomm.services.interfaces.ILoginTicketService;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

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
    private IUserService userService;

    @Autowired
    public BillingController(final ILoginTicketService loginTicketService, 
                             final IAfipService afipService,
                             final IBillService billService,
                             final ICheckoutService checkoutService,
                             final IUserService userService) {
        this.loginTicketService = loginTicketService;
        this.afipService = afipService;
        this.billService = billService;
        this.checkoutService = checkoutService;
        this.userService = userService;
    }

    @GetMapping("/person/{CUIT}")
    public ResponseEntity<ResponsePerson> getByCUIT(@PathVariable String CUIT) {
        final LoginTicketResponse ticketResponse = afipService.get("ws_sr_padron_a5");
        ResponsePerson person = billService.createPersonRequest(ticketResponse.getToken(),
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
        LastBillIdResponse lastBillId = billService.getLastBillId(ticketResponse, lastBillIdRequest, lastBillIdRequest.getBillType());
        lastBillId.setBillType(lastBillIdRequest.getBillType());
        lastBillId.setCuit(lastBillIdRequest.getCuit());

        return ResponseEntity.ok(lastBillId);
    }

    @PostMapping
    public ResponseEntity<?> create(final @RequestBody BillRequest billRequest) {
        LoginTicketResponse ticketResponse = afipService.get("wsfe");
        BillResponse billResponse = billService.createBilling(ticketResponse, billRequest);

        if (billResponse.getMessage() != null && !billResponse.getResultado().equalsIgnoreCase("A")) {
            return ResponseEntity.badRequest().body(billResponse.getMessage().getMessage());
        }
        Checkout checkout = checkoutService.changeStateTo(CheckoutState.PAID_OUT, billRequest.getCheckoutId());
        checkout.setUsername(billRequest.getUsername());
        Bill bill = billService.save(billResponse, checkout);

        return ResponseEntity.ok(bill);
    }

    @GetMapping
    public ResponseEntity<List<Bill>> findAll() {
        List<Bill> bills = billService.findAll();

        return ResponseEntity.ok(bills);
    }

    @GetMapping("{id}")
    public ResponseEntity<Bill> get(final @PathVariable Long id) {
        Bill bill = billService.get(id);

        return ResponseEntity.ok(bill);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Bill>> getByCheckoutId(final @PathVariable String username) {
        final User user = userService.get(username);
        List<Bill> bills = billService.findAllByUser(user);

        return ResponseEntity.ok(bills);
    }

    @GetMapping("/billTypes")
    public ResponseEntity<List<BillType>> getByCheckoutId() {
        LoginTicketResponse ticketResponse = afipService.get("wsfe");
        List<BillType> billsTypes = Arrays.asList(BillType.values());
        billService.getBillTypes(ticketResponse);

        return ResponseEntity.ok(billsTypes);
    }

    @PostMapping("search")
    public ResponseEntity<List<Bill>> search(@RequestBody BillResponse response) {
        List<Bill> bills =  billService.search(response);

        return ResponseEntity.ok(bills);
    }
}
