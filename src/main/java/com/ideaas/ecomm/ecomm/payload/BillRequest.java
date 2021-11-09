package com.ideaas.ecomm.ecomm.payload;

import com.ideaas.ecomm.ecomm.domain.Cart;
import com.ideaas.ecomm.ecomm.domain.Item;
import com.ideaas.ecomm.ecomm.enums.BillType;
import com.ideaas.ecomm.ecomm.enums.IVAConditionType;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("all")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class BillRequest {

    private Long id;

    private String cardId;

    private BillType billType;

    private IVAConditionType ivaConditionType;

    private int puntoDeVenta;

    private LocalDate date = LocalDate.now();

    private String cuit;

    private String comments;

    private List<Item> items;

    private Long checkoutId;

    private Cart cart;

    public Double getTotalAmount() {
        return items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

    public Double getSubtotal() {
        return  items.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
    }

}
