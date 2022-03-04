package com.ideaas.ecomm.ecomm.payload.bill;

import com.ideaas.ecomm.ecomm.payload.FeCabResp;
import com.ideaas.ecomm.ecomm.payload.FeDetResp;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@XmlRootElement(name = "FECAESolicitarResult")
public class FECAESolicitarResult {

    @XmlAttribute
    private FeDetResp feDetResp;

    @XmlElement(name = "FeCabResp")
    private FeCabResp feCabResp;

}
