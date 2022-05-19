
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
 *         &lt;element name="FECAEASinMovimientoInformarResult" type="{http://ar.gov.afip.dif.FEV1/}FECAEASinMovResponse" minOccurs="0"/&gt;
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
    "fecaeaSinMovimientoInformarResult"
})
@XmlRootElement(name = "FECAEASinMovimientoInformarResponse")
public class FECAEASinMovimientoInformarResponse {

    @XmlElement(name = "FECAEASinMovimientoInformarResult")
    protected FECAEASinMovResponse fecaeaSinMovimientoInformarResult;

    /**
     * Gets the value of the fecaeaSinMovimientoInformarResult property.
     * 
     * @return
     *     possible object is
     *     {@link FECAEASinMovResponse }
     *     
     */
    public FECAEASinMovResponse getFECAEASinMovimientoInformarResult() {
        return fecaeaSinMovimientoInformarResult;
    }

    /**
     * Sets the value of the fecaeaSinMovimientoInformarResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEASinMovResponse }
     *     
     */
    public void setFECAEASinMovimientoInformarResult(FECAEASinMovResponse value) {
        this.fecaeaSinMovimientoInformarResult = value;
    }

}
