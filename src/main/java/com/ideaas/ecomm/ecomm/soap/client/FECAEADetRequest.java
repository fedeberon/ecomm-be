
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para FECAEADetRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="FECAEADetRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ar.gov.afip.dif.FEV1/}FEDetRequest"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CAEA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="CbteFchHsGen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FECAEADetRequest", propOrder = {
    "caea",
    "cbteFchHsGen"
})
public class FECAEADetRequest
    extends FEDetRequest
{

    @XmlElement(name = "CAEA")
    protected String caea;
    @XmlElement(name = "CbteFchHsGen")
    protected String cbteFchHsGen;

    /**
     * Obtiene el valor de la propiedad caea.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAEA() {
        return caea;
    }

    /**
     * Define el valor de la propiedad caea.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAEA(String value) {
        this.caea = value;
    }

    /**
     * Obtiene el valor de la propiedad cbteFchHsGen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCbteFchHsGen() {
        return cbteFchHsGen;
    }

    /**
     * Define el valor de la propiedad cbteFchHsGen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCbteFchHsGen(String value) {
        this.cbteFchHsGen = value;
    }

}
