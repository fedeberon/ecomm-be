
package com.ideaas.ecomm.ecomm.soap.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfDocTipo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfDocTipo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DocTipo" type="{http://ar.gov.afip.dif.FEV1/}DocTipo" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfDocTipo", propOrder = {
    "docTipo"
})
public class ArrayOfDocTipo {

    @XmlElement(name = "DocTipo", nillable = true)
    protected List<DocTipo> docTipo;

    /**
     * Gets the value of the docTipo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the docTipo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocTipo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DocTipo }
     * 
     * 
     */
    public List<DocTipo> getDocTipo() {
        if (docTipo == null) {
            docTipo = new ArrayList<DocTipo>();
        }
        return this.docTipo;
    }

}
