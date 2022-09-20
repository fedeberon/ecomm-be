
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FEPaisResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FEPaisResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ResultGet" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfPaisTipo" minOccurs="0"/&gt;
 *         &lt;element name="Errors" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfErr" minOccurs="0"/&gt;
 *         &lt;element name="Events" type="{http://ar.gov.afip.dif.FEV1/}ArrayOfEvt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FEPaisResponse", propOrder = {
    "resultGet",
    "errors",
    "events"
})
public class FEPaisResponse {

    @XmlElement(name = "ResultGet")
    protected ArrayOfPaisTipo resultGet;
    @XmlElement(name = "Errors")
    protected ArrayOfErr errors;
    @XmlElement(name = "Events")
    protected ArrayOfEvt events;

    /**
     * Gets the value of the resultGet property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPaisTipo }
     *     
     */
    public ArrayOfPaisTipo getResultGet() {
        return resultGet;
    }

    /**
     * Sets the value of the resultGet property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPaisTipo }
     *     
     */
    public void setResultGet(ArrayOfPaisTipo value) {
        this.resultGet = value;
    }

    /**
     * Gets the value of the errors property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfErr }
     *     
     */
    public ArrayOfErr getErrors() {
        return errors;
    }

    /**
     * Sets the value of the errors property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfErr }
     *     
     */
    public void setErrors(ArrayOfErr value) {
        this.errors = value;
    }

    /**
     * Gets the value of the events property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfEvt }
     *     
     */
    public ArrayOfEvt getEvents() {
        return events;
    }

    /**
     * Sets the value of the events property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfEvt }
     *     
     */
    public void setEvents(ArrayOfEvt value) {
        this.events = value;
    }

}
