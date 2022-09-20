
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
 *         &lt;element name="FEParamGetTiposDocResult" type="{http://ar.gov.afip.dif.FEV1/}DocTipoResponse" minOccurs="0"/&gt;
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
    "feParamGetTiposDocResult"
})
@XmlRootElement(name = "FEParamGetTiposDocResponse")
public class FEParamGetTiposDocResponse {

    @XmlElement(name = "FEParamGetTiposDocResult")
    protected DocTipoResponse feParamGetTiposDocResult;

    /**
     * Gets the value of the feParamGetTiposDocResult property.
     * 
     * @return
     *     possible object is
     *     {@link DocTipoResponse }
     *     
     */
    public DocTipoResponse getFEParamGetTiposDocResult() {
        return feParamGetTiposDocResult;
    }

    /**
     * Sets the value of the feParamGetTiposDocResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocTipoResponse }
     *     
     */
    public void setFEParamGetTiposDocResult(DocTipoResponse value) {
        this.feParamGetTiposDocResult = value;
    }

}
