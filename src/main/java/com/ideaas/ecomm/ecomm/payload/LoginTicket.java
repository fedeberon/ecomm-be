package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.AFIP.LoginTicketResponse;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "loginTicketResponse")
public class LoginTicket {

    @XmlElement(name = "credentials")
    private Credential credential;

    @XmlElement(name = "header")
    private Header header;


    public LoginTicketResponse build() {
        return new LoginTicketResponse(this);
    }

}