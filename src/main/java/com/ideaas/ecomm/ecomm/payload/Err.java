package com.ideaas.ecomm.ecomm.payload;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Errors")
@XmlAccessorType(XmlAccessType.FIELD)
public class Err {

    @XmlElement(name = "Code")
    private String code;

    @XmlElement(name = "Msg")
    private String message;
}
