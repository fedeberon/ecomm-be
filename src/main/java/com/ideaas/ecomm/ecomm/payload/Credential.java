package com.ideaas.ecomm.ecomm.payload;


import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "credentials")
public class Credential {

    @XmlElement(name="token")
    private String token;

    @XmlElement(name="sign")
    private String sign;

}
