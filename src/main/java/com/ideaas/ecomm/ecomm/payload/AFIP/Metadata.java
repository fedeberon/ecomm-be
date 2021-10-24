package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadata  implements java.io.Serializable {

    private Integer id;

    @XmlElement
    private java.util.Calendar fechaHora;

    @XmlElement
    private java.lang.String servidor;

    public Metadata() { }

    public Metadata(
            java.util.Calendar fechaHora,
            java.lang.String servidor) {
        this.fechaHora = fechaHora;
        this.servidor = servidor;
    }

}
