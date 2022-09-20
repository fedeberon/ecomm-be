
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
 *         &lt;element name="FECompConsultarResult" type="{http://ar.gov.afip.dif.FEV1/}FECompConsultaResponse" minOccurs="0"/&gt;
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
    "feCompConsultarResult"
})
@XmlRootElement(name = "FECompConsultarResponse")
public class FECompConsultarResponse {

    @XmlElement(name = "FECompConsultarResult")
    protected FECompConsultaResponse feCompConsultarResult;

    /**
     * Gets the value of the feCompConsultarResult property.
     * 
     * @return
     *     possible object is
     *     {@link FECompConsultaResponse }
     *     
     */
    public FECompConsultaResponse getFECompConsultarResult() {
        return feCompConsultarResult;
    }

    /**
     * Sets the value of the feCompConsultarResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FECompConsultaResponse }
     *     
     */
    public void setFECompConsultarResult(FECompConsultaResponse value) {
        this.feCompConsultarResult = value;
    }

}
