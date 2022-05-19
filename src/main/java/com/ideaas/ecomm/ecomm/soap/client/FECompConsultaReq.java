
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FECompConsultaReq complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FECompConsultaReq"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CbteTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="CbteNro" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECompConsultaReq", propOrder = {
    "cbteTipo",
    "cbteNro",
    "ptoVta"
})
public class FECompConsultaReq {

    @XmlElement(name = "CbteTipo")
    protected int cbteTipo;
    @XmlElement(name = "CbteNro")
    protected long cbteNro;
    @XmlElement(name = "PtoVta")
    protected int ptoVta;

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

    /**
     * Gets the value of the cbteNro property.
     * 
     */
    public long getCbteNro() {
        return cbteNro;
    }

    /**
     * Sets the value of the cbteNro property.
     * 
     */
    public void setCbteNro(long value) {
        this.cbteNro = value;
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

}
