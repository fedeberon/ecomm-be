package com.ideaas.ecomm.ecomm.payload.AFIP;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement(name = "datosGenerales")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@Entity
@Table(name = "PEOPLE")
public class Person {

    @Id
    @Column(name = "PER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @XmlElement(name = "nombre")
    @Column(name = "PER_NOMBRE")
    private String name;

    @XmlElement(name = "apellido")
    @Column(name = "PER_APELLIDO")
    private String lastName;

    @Column(name = "iva")
    private String iva;

    @Column(name = "monotributo")
    private String monotributo;

    @XmlElement(name = "tipoPersona")
    @Enumerated(EnumType.STRING)
    @Column(name = "PER_TIPO_PERSONA")
    private PersonTypeEnum personType;

    @XmlElement
    @Column(name = "PER_DESCRIPCION_ACTIVIDAD_PRINCIPAL")
    private String description;

    @XmlElement(name = "domicilioFiscal")
    @OneToMany(cascade = {CascadeType.PERSIST}, mappedBy = "persona", fetch = FetchType.LAZY)
    private Set<Address> addresses;

    @XmlElement
    @Transient
    private String KeyState;

    @XmlElement(name = "idPersona")
    @Column(name = "PER_ID_PERSONA")
    private Long personId;

    @XmlElement
    @Column(name = "PER_NUMERO_DOCUMENTO")
    private String cardId;

}
