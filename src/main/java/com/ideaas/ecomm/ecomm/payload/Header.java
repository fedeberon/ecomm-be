package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.adapters.LocalDateTimeAdapter;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public  class Header {

    @XmlElement(name="uniqueId")
    private String uniqueId;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name="generationTime")
    private LocalDateTime generationTime;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @XmlElement(name="expirationTime")
    private LocalDateTime expirationTime;

}