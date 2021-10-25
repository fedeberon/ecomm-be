package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "FeDetResp" )
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class FeDetResp {

    @XmlElement(name = "FEDetResponse")
    private FEDetResponse fEDetResponse;

}
