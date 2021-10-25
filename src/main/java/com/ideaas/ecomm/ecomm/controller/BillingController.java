package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.payload.AFIP.FECAE;
import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.payload.AFIP.Person;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("billing")
public class BillingController {

    private IAfipService afipService;
    private IBillService billService;

    @Autowired
    public BillingController(IAfipService afipService, IBillService billService) {
        this.afipService = afipService;
        this.billService = billService;
    }

    @RequestMapping("{CUIT}")
    public ResponseEntity<Person> getByCUIT(@PathVariable String CUIT) {
        LoginTicketResponse ticketResponse = afipService.getAuthentication();
        Person person = billService.createPersonRequest("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiB1bmlxdWVfaWQ9IjI1MTIyNzI3MTUiIGdlbl90aW1lPSIxNjM1MDgyODE2IiBleHBfdGltZT0iMTYzNTEyNjA3NiIvPgogICAgPG9wZXJhdGlvbiB0eXBlPSJsb2dpbiIgdmFsdWU9ImdyYW50ZWQiPgogICAgICAgIDxsb2dpbiBlbnRpdHk9IjMzNjkzNDUwMjM5IiBzZXJ2aWNlPSJ3c19zcl9wYWRyb25fYTUiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjAyODU2NDA2NjEsIENOPWZlZGViZXJvbiIgYXV0aG1ldGhvZD0iY21zIiByZWdtZXRob2Q9IjIyIj4KICAgICAgICAgICAgPHJlbGF0aW9ucz4KICAgICAgICAgICAgICAgIDxyZWxhdGlvbiBrZXk9IjIwMjg1NjQwNjYxIiByZWx0eXBlPSI0Ii8+CiAgICAgICAgICAgIDwvcmVsYXRpb25zPgogICAgICAgIDwvbG9naW4+CiAgICA8L29wZXJhdGlvbj4KPC9zc28+Cg==",
                                                        "FjpLwv0/hiJSJ/lHNG7o5WAv5Riab02SuTFCp8XS/ZnPJphw9qZ6IBkL/C2ikxUut45TA+xbkDKwhU8O0UwUiRxCGJxcMNkoH4NJG4S0MURePGfacPGbFpI8aBOgMtaJwcOcvEk4PXYEBWFL3p8zpixG194DcfjEtFgvGS5fFLo=",
                                         "20285640661",
                                                        CUIT);

        return ResponseEntity.ok(person);
    }

    @PostMapping
    public ResponseEntity<FECAE> prepareBill() {
        LoginTicketResponse ticketResponse = new LoginTicketResponse();
        ticketResponse.setToken("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiB1bmlxdWVfaWQ9IjI5ODc3MzQ0MCIgZ2VuX3RpbWU9IjE2MzUyMDE5NDEiIGV4cF90aW1lPSIxNjM1MjQ1MjAxIi8+CiAgICA8b3BlcmF0aW9uIHR5cGU9ImxvZ2luIiB2YWx1ZT0iZ3JhbnRlZCI+CiAgICAgICAgPGxvZ2luIGVudGl0eT0iMzM2OTM0NTAyMzkiIHNlcnZpY2U9IndzX3NyX3BhZHJvbl9hNSIgdWlkPSJTRVJJQUxOVU1CRVI9Q1VJVCAyMDI4NTY0MDY2MSwgQ049ZmVkZWJlcm9uIiBhdXRobWV0aG9kPSJjbXMiIHJlZ21ldGhvZD0iMjIiPgogICAgICAgICAgICA8cmVsYXRpb25zPgogICAgICAgICAgICAgICAgPHJlbGF0aW9uIGtleT0iMjAyODU2NDA2NjEiIHJlbHR5cGU9IjQiLz4KICAgICAgICAgICAgPC9yZWxhdGlvbnM+CiAgICAgICAgPC9sb2dpbj4KICAgIDwvb3BlcmF0aW9uPgo8L3Nzbz4K");
        ticketResponse.setSign("fnWM7Vnqw5louyzb6eWj6NmZIUdg9Uuwr9eBWSlaSz1Rw7pUwZKXBZ7nsm/aWDnIgNKdImMqShlc3K0H3g6Qnuv0qdN2BxWwXpNgtIV4an8F8E++AHSiossqqge1uTODcWJOaztVD/Kl75XiHqLIbPI+0390q/4Y6Tj7R5CgOCg=");
        final FECAE fecae = billService.createCAERequest(ticketResponse, "30334719531");

        return ResponseEntity.ok(fecae);
    }

}
