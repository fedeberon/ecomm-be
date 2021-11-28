package com.ideaas.ecomm.ecomm.converts.exceptions;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Errors {

    @XmlElement(name = "codigoDescripcion")
    private List<DescriptionCode> descriptions;


    @XmlAccessorType(XmlAccessType.FIELD)
    @Getter
    @Setter
    public static class DescriptionCode {

        @XmlElement(name = "codigo")
        private String code;

        @XmlElement(name = "descripcion")
        private String description;

    }

}
