package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "FeCabResp" )
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class FeCabResp {

    @XmlElement(name = "Cuit")
    private String cuit;

    @XmlElement(name = "PtoVta")
    private String ptoVta;

    @XmlElement(name = "FchProceso")
    private String fchProceso;

    @XmlElement(name = "CantReg")
    private String cantReg;

    @XmlElement(name = "Resultado")
    private String resultado;

    @XmlElement(name = "Reproceso")
    private String reproceso;


}
