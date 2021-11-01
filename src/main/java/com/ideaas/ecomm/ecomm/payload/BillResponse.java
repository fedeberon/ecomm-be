package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.AFIP.Voucher;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
public class BillResponse {

    @XmlElement(name = "resultado")
    private String result;

    @XmlElement(name = "comprobanteResponse")
    private Voucher voucher;

}
