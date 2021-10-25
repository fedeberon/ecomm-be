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
public class FEDetResponse {

    @XmlElement(name = "Concepto")
    private String concepto;

    @XmlElement(name = "DocTipo")
    private String cocTipo;

    @XmlElement(name = "DocNro")
    private String docNro;

    @XmlElement(name = "Resultado")
    private String resultado;

    @XmlElement(name = "CAE")
    private String CAE;

    @XmlElement(name = "CbteFch")
    private String cbteFch;

    @XmlElement(name = "CAEFchVto")
    private String cAEFchVto;

}
