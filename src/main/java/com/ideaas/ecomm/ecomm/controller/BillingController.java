package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.payload.AFIP.LoginTicketResponse;
import com.ideaas.ecomm.ecomm.payload.AFIP.Person;
import com.ideaas.ecomm.ecomm.services.interfaces.IAfipService;
import com.ideaas.ecomm.ecomm.services.interfaces.IBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
        //LoginTicketResponse ticketResponse = afipService.getAuthentication();
        Person person = billService.createPersonRequest("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgc3JjPSJDTj13c2FhLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiB1bmlxdWVfaWQ9IjI1MTIyNzI3MTUiIGdlbl90aW1lPSIxNjM1MDgyODE2IiBleHBfdGltZT0iMTYzNTEyNjA3NiIvPgogICAgPG9wZXJhdGlvbiB0eXBlPSJsb2dpbiIgdmFsdWU9ImdyYW50ZWQiPgogICAgICAgIDxsb2dpbiBlbnRpdHk9IjMzNjkzNDUwMjM5IiBzZXJ2aWNlPSJ3c19zcl9wYWRyb25fYTUiIHVpZD0iU0VSSUFMTlVNQkVSPUNVSVQgMjAyODU2NDA2NjEsIENOPWZlZGViZXJvbiIgYXV0aG1ldGhvZD0iY21zIiByZWdtZXRob2Q9IjIyIj4KICAgICAgICAgICAgPHJlbGF0aW9ucz4KICAgICAgICAgICAgICAgIDxyZWxhdGlvbiBrZXk9IjIwMjg1NjQwNjYxIiByZWx0eXBlPSI0Ii8+CiAgICAgICAgICAgIDwvcmVsYXRpb25zPgogICAgICAgIDwvbG9naW4+CiAgICA8L29wZXJhdGlvbj4KPC9zc28+Cg==",
                                                        "FjpLwv0/hiJSJ/lHNG7o5WAv5Riab02SuTFCp8XS/ZnPJphw9qZ6IBkL/C2ikxUut45TA+xbkDKwhU8O0UwUiRxCGJxcMNkoH4NJG4S0MURePGfacPGbFpI8aBOgMtaJwcOcvEk4PXYEBWFL3p8zpixG194DcfjEtFgvGS5fFLo=",
                                         "20285640661",
                                                        CUIT);

        return ResponseEntity.ok(person);
    }

}
