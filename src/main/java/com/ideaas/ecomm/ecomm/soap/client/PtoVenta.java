
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PtoVenta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PtoVenta"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Nro" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="EmisionTipo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Bloqueado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FchBaja" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PtoVenta", propOrder = {
    "nro",
    "emisionTipo",
    "bloqueado",
    "fchBaja"
})
public class PtoVenta {

    @XmlElement(name = "Nro")
    protected int nro;
    @XmlElement(name = "EmisionTipo")
    protected String emisionTipo;
    @XmlElement(name = "Bloqueado")
    protected String bloqueado;
    @XmlElement(name = "FchBaja")
    protected String fchBaja;

    /**
     * Gets the value of the nro property.
     * 
     */
    public int getNro() {
        return nro;
    }

    /**
     * Sets the value of the nro property.
     * 
     */
    public void setNro(int value) {
        this.nro = value;
    }

    /**
     * Gets the value of the emisionTipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmisionTipo() {
        return emisionTipo;
    }

    /**
     * Sets the value of the emisionTipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmisionTipo(String value) {
        this.emisionTipo = value;
    }

    /**
     * Gets the value of the bloqueado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBloqueado() {
        return bloqueado;
    }

    /**
     * Sets the value of the bloqueado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBloqueado(String value) {
        this.bloqueado = value;
    }

    /**
     * Gets the value of the fchBaja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFchBaja() {
        return fchBaja;
    }

    /**
     * Sets the value of the fchBaja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFchBaja(String value) {
        this.fchBaja = value;
    }

}
