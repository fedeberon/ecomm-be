
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FECAEDetResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FECAEDetResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ar.gov.afip.dif.FEV1/}FEDetResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CAEFchVto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEDetResponse", propOrder = {
    "cae",
    "caeFchVto"
})
public class FECAEDetResponse
    extends FEDetResponse
{

    @XmlElement(name = "CAE")
    protected String cae;
    @XmlElement(name = "CAEFchVto")
    protected String caeFchVto;

    /**
     * Gets the value of the cae property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAE() {
        return cae;
    }

    /**
     * Sets the value of the cae property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAE(String value) {
        this.cae = value;
    }

    /**
     * Gets the value of the caeFchVto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAEFchVto() {
        return caeFchVto;
    }

    /**
     * Sets the value of the caeFchVto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAEFchVto(String value) {
        this.caeFchVto = value;
    }

}
