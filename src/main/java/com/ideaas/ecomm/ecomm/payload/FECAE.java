package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "FECAESolicitarResult" )
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class FECAE {

    @XmlElement(name = "FeCabResp")
    private FeCabResp feCabResp;

    @XmlElement(name = "FeDetResp")
    private FeDetResp feDetResp;

}
