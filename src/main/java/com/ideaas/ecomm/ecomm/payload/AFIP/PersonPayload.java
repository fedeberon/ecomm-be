package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Component
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "personaReturn")
public class PersonPayload implements java.io.Serializable {

    private Integer id;

    @XmlElement(name = "metadata")
    private Metadata metadata;

    @XmlElement(name = "datosGenerales")
    private Person person;


}
