package com.ideaas.ecomm.ecomm.payload;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Errors {

    @XmlElement(name = "Code")
    private String code;

    @XmlElement(name = "Msg")
    private String message;
}
