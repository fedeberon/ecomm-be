package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.enums.BillType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "FECompUltimoAutorizadoResponse")
@NoArgsConstructor
public class LastBillIdResponse {

    @XmlElement(name = "CbteNro")
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

    @Override
    public String toString() {
        return "LastBillIdResponse{" +
                "lastId='" + lastId + '\'' +
                ", cuit='" + cuit + '\'' +
                ", billType=" + billType +
                '}';
    }

}
