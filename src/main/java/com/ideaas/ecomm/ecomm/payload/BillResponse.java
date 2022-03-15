package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.Checkout;
import com.ideaas.ecomm.ecomm.enums.BillType;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FECAEDetResponse")
public class BillResponse {

    @XmlElement(name = "Concepto")
    private String concepto;

    @XmlElement(name = "DocTipo")
    private String docTipo;

    @XmlElement(name = "DocNro")
    private String docNro;

    @XmlElement(name = "CbteDesde")
    private String cbteDesde;

    @XmlElement(name = "CbteHasta")
    private String cbteHasta;

    @XmlElement(name = "CbteFch")
    private String cbteFch;

    @XmlElement(name = "Resultado")
    private String resultado;

    @XmlElement(name = "CAE")
    private String CAE;

    @XmlElement(name = "CAEFchVto")
    private String CAEFchVto;

    private BillType billType;

    private Long nroComprobante;

    private int puntoDeVenta;

    private String creditCard;

    private String coupon;

    private String CUIT;


}
