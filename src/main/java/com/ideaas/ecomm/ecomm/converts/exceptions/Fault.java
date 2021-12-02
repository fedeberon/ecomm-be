package com.ideaas.ecomm.ecomm.converts.exceptions;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Fault {

    @XmlElement(name = "faultcode")
    private String code;

    @XmlElement(name = "faultstring")
    private String detail;
}