
package com.ideaas.ecomm.ecomm.soap.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfOpcional complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfOpcional"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Opcional" type="{http://ar.gov.afip.dif.FEV1/}Opcional" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfOpcional", propOrder = {
    "opcional"
})
public class ArrayOfOpcional {

    @XmlElement(name = "Opcional", nillable = true)
    protected List<Opcional> opcional;

    /**
     * Gets the value of the opcional property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the opcional property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOpcional().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Opcional }
     * 
     * 
     */
    public List<Opcional> getOpcional() {
        if (opcional == null) {
            opcional = new ArrayList<Opcional>();
        }
        return this.opcional;
    }

}
