
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Auth" type="{http://ar.gov.afip.dif.FEV1/}FEAuthRequest" minOccurs="0"/&gt;
 *         &lt;element name="Periodo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Orden" type="{http://www.w3.org/2001/XMLSchema}short"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "auth",
    "periodo",
    "orden"
})
@XmlRootElement(name = "FECAEAConsultar")
public class FECAEAConsultar {

    @XmlElement(name = "Auth")
    protected FEAuthRequest auth;
    @XmlElement(name = "Periodo")
    protected int periodo;
    @XmlElement(name = "Orden")
    protected short orden;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link FEAuthRequest }
     *     
     */
    public FEAuthRequest getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link FEAuthRequest }
     *     
     */
    public void setAuth(FEAuthRequest value) {
        this.auth = value;
    }

    /**
     * Gets the value of the periodo property.
     * 
     */
    public int getPeriodo() {
        return periodo;
    }

    /**
     * Sets the value of the periodo property.
     * 
     */
    public void setPeriodo(int value) {
        this.periodo = value;
    }

    /**
     * Gets the value of the orden property.
     * 
     */
    public short getOrden() {
        return orden;
    }

    /**
     * Sets the value of the orden property.
     * 
     */
    public void setOrden(short value) {
        this.orden = value;
    }

}
