package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.BillType;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class Voucher {


    @XmlElement(name = "cuit")
    private String cuit;

    @XmlElement(name = "codigoTipoComprobante")
    private BillType billType;

    @XmlElement(name = "numeroPuntoVenta")
    private int pointNumber;

    @XmlElement(name = "numeroComprobante")
    private Long number;

    @XmlElement(name = "fechaEmision")
    private String date;

    @XmlElement(name = "CAE")
    private String CAE;

    @XmlElement(name = "fechaVencimientoCAE")
    private String dueDateCAE;


}
