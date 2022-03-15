package com.ideaas.ecomm.ecomm.payload.bill;

import com.ideaas.ecomm.ecomm.payload.FEHeaderInfo;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Header {

    @XmlElement(name = "FEHeaderInfo")
    private FEHeaderInfo fEHeaderInfo;

}
