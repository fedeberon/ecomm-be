
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FECAEASolicitarResult" type="{http://ar.gov.afip.dif.FEV1/}FECAEAGetResponse" minOccurs="0"/&gt;
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
    "fecaeaSolicitarResult"
})
@XmlRootElement(name = "FECAEASolicitarResponse")
public class FECAEASolicitarResponse {

    @XmlElement(name = "FECAEASolicitarResult")
    protected FECAEAGetResponse fecaeaSolicitarResult;

    /**
     * Obtiene el valor de la propiedad fecaeaSolicitarResult.
     * 
     * @return
     *     possible object is
     *     {@link FECAEAGetResponse }
     *     
     */
    public FECAEAGetResponse getFECAEASolicitarResult() {
        return fecaeaSolicitarResult;
    }

    /**
     * Define el valor de la propiedad fecaeaSolicitarResult.
     * 
     * @param value
     *     allowed object is
     *     {@link FECAEAGetResponse }
     *     
     */
    public void setFECAEASolicitarResult(FECAEAGetResponse value) {
        this.fecaeaSolicitarResult = value;
    }

}
