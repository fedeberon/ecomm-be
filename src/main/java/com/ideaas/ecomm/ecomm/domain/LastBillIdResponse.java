package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.BillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
public class LastBillIdResponse {

    @XmlElement(name = "numeroComprobante")
    private String lastId;

    private String cuit;

    private BillType billType;

    public LastBillIdResponse(final String cuit, final BillType billType) {
        this.cuit = cuit;
        this.billType = billType;
    }

    public Integer nextBillId() {
        Integer id = Integer.parseInt(this.lastId);

        return id + 1;
    }
}
