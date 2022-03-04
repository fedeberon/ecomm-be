package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class FEHeaderInfo {

    @XmlElement(name = "ambiente")
    private String ambiente;

    @XmlElement(name = "fecha")
    private String fecha;

    @XmlElement(name = "id")
    private String id;


}

