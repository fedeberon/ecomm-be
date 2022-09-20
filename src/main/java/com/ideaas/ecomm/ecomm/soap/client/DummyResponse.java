
package com.ideaas.ecomm.ecomm.soap.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DummyResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DummyResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AppServer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DbServer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AuthServer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DummyResponse", propOrder = {
    "appServer",
    "dbServer",
    "authServer"
})
public class DummyResponse {

    @XmlElement(name = "AppServer")
    protected String appServer;
    @XmlElement(name = "DbServer")
    protected String dbServer;
    @XmlElement(name = "AuthServer")
    protected String authServer;

    /**
     * Gets the value of the appServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppServer() {
        return appServer;
    }

    /**
     * Sets the value of the appServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppServer(String value) {
        this.appServer = value;
    }

    /**
     * Gets the value of the dbServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDbServer() {
        return dbServer;
    }

    /**
     * Sets the value of the dbServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDbServer(String value) {
        this.dbServer = value;
    }

    /**
     * Gets the value of the authServer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthServer() {
        return authServer;
    }

    /**
     * Sets the value of the authServer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthServer(String value) {
        this.authServer = value;
    }

}
