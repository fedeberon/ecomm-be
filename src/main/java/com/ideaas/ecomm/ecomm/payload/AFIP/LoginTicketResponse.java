package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "loginTicketResponse")
@Table(name="CREDENCIAL_AUTENTICACION_AFIP")
public class LoginTicketResponse {

    @XmlElement(name = "uniqueId")
    @Id
    @Column(name="uniqueId")
    private String uniqueId;

    @XmlElement(name="token")
    @Column(name="token")
    private String token;

    @XmlElement(name="sign")
    @Column(name = "sign")
    private String sign;

    @XmlElement(name="generationTime")
    @Column(name = "fechaDePedido")
    private String generationTime;

    @XmlElement(name="expirationTime")
    @Column(name = "fechaDeCaducacion")
    private String expirationTime;

}


