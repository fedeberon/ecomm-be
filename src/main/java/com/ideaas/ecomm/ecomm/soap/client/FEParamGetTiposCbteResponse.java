
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
 *         &lt;element name="FEParamGetTiposCbteResult" type="{http://ar.gov.afip.dif.FEV1/}CbteTipoResponse" minOccurs="0"/&gt;
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
    "feParamGetTiposCbteResult"
})
@XmlRootElement(name = "FEParamGetTiposCbteResponse")
public class FEParamGetTiposCbteResponse {

    @XmlElement(name = "FEParamGetTiposCbteResult")
    protected CbteTipoResponse feParamGetTiposCbteResult;

    /**
     * Gets the value of the feParamGetTiposCbteResult property.
     * 
     * @return
     *     possible object is
     *     {@link CbteTipoResponse }
     *     
     */
    public CbteTipoResponse getFEParamGetTiposCbteResult() {
        return feParamGetTiposCbteResult;
    }

    /**
     * Sets the value of the feParamGetTiposCbteResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link CbteTipoResponse }
     *     
     */
    public void setFEParamGetTiposCbteResult(CbteTipoResponse value) {
        this.feParamGetTiposCbteResult = value;
    }

}
