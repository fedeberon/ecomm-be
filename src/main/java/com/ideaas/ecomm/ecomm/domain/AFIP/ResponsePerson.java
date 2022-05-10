package com.ideaas.ecomm.ecomm.domain.AFIP;

import com.ideaas.ecomm.ecomm.converts.exceptions.Fault;
import com.ideaas.ecomm.ecomm.payload.PersonPayload;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponsePerson {

    private PersonPayload personPayload;
    private Fault fault;

}
