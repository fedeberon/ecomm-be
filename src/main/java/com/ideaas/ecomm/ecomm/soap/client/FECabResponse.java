
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FECabResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FECabResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Cuit" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="PtoVta" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="CbteTipo" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="FchProceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CantReg" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Resultado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Reproceso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECabResponse", propOrder = {
    "cuit",
    "ptoVta",
    "cbteTipo",
    "fchProceso",
    "cantReg",
    "resultado",
    "reproceso"
})
@XmlSeeAlso({
    FECAECabResponse.class,
    FECAEACabResponse.class
})
public class FECabResponse {

    @XmlElement(name = "Cuit")
    protected long cuit;
    @XmlElement(name = "PtoVta")
    protected int ptoVta;
    @XmlElement(name = "CbteTipo")
    protected int cbteTipo;
    @XmlElement(name = "FchProceso")
    protected String fchProceso;
    @XmlElement(name = "CantReg")
    protected int cantReg;
    @XmlElement(name = "Resultado")
    protected String resultado;
    @XmlElement(name = "Reproceso")
    protected String reproceso;

    /**
     * Gets the value of the cuit property.
     * 
     */
    public long getCuit() {
        return cuit;
    }

    /**
     * Sets the value of the cuit property.
     * 
     */
    public void setCuit(long value) {
        this.cuit = value;
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

    /**
     * Gets the value of the fchProceso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFchProceso() {
        return fchProceso;
    }

    /**
     * Sets the value of the fchProceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFchProceso(String value) {
        this.fchProceso = value;
    }

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
     * Gets the value of the resultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultado() {
        return resultado;
    }

    /**
     * Sets the value of the resultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultado(String value) {
        this.resultado = value;
    }

    /**
     * Gets the value of the reproceso property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReproceso() {
        return reproceso;
    }

    /**
     * Sets the value of the reproceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReproceso(String value) {
        this.reproceso = value;
    }

}
