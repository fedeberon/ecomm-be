
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FECabRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FECabRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CantReg" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="CbteTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECabRequest", propOrder = {
    "cantReg",
    "ptoVta",
    "cbteTipo"
})
@XmlSeeAlso({
    FECAECabRequest.class,
    FECAEACabRequest.class
})
public class FECabRequest {

    @XmlElement(name = "CantReg")
    protected int cantReg;
    @XmlElement(name = "PtoVta")
    protected int ptoVta;
    @XmlElement(name = "CbteTipo")
    protected int cbteTipo;

    /**
     * Gets the value of the cantReg property.
     * 
     */
    public int getCantReg() {
        return cantReg;
    }

    /**
     * Sets the value of the cantReg property.
     * 
     */
    public void setCantReg(int value) {
        this.cantReg = value;
    }

    /**
     * Gets the value of the ptoVta property.
     * 
     */
    public int getPtoVta() {
        return ptoVta;
    }

    /**
     * Sets the value of the ptoVta property.
     * 
     */
    public void setPtoVta(int value) {
        this.ptoVta = value;
    }

    /**
     * Gets the value of the cbteTipo property.
     * 
     */
    public int getCbteTipo() {
        return cbteTipo;
    }

    /**
     * Sets the value of the cbteTipo property.
     * 
     */
    public void setCbteTipo(int value) {
        this.cbteTipo = value;
    }

}
