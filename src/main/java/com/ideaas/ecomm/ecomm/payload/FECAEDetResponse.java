package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@XmlRootElement(name = "FECAEDetResponse")
public class FECAEDetResponse {

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

}

