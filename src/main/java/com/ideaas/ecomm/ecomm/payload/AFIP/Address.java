package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "domicilioFiscal" )
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@Entity
@Table(name = "ADDRESSES")
public class Address {

    @Id
    @Column(name = "DOM_ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @XmlElement
    @Column(name = "DOM_COD_POSTAL")
    private java.lang.String codPostal;

    @XmlElement
    private java.lang.String datoAdicional;

    @XmlElement
    private java.lang.String descripcionProvincia;

    @XmlElement
    @Column(name = "DOM_DIRECCION")
    private java.lang.String direccion;

    @XmlElement
    private java.lang.Integer idProvincia;

    @XmlElement
    @Column(name = "DOM_LOCALIDAD")
    private java.lang.String localidad;

    @XmlElement
    private java.lang.String tipoDatoAdicional;

    @XmlElement
    @Column(name = "DOM_TIPODOMICILIO")
    private java.lang.String tipoDomicilio;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "DOM_PER_ID")
    private Person persona;

}
