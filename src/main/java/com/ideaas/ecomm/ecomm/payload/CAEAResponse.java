package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.LocalDate;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class CAEAResponse {

    @XmlElement(name = "fechaProceso")
    private LocalDate date;

    @XmlElement(name = "CAEA")
    private long caea;

    @XmlElement(name = "periodo")
    private int periodo;

    @XmlElement(name = "orden")
    private String orden;

    private String cuit;
}
