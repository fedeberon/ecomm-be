package com.ideaas.ecomm.ecomm.domain;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
public class LastBillIdResponse {

    @XmlElement(name = "Id")
    private String lastId;

    private String cuit;

}
