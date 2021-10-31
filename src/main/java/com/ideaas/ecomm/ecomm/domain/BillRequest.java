package com.ideaas.ecomm.ecomm.domain;

import com.ideaas.ecomm.ecomm.enums.BillType;
import com.ideaas.ecomm.ecomm.enums.IVAConditionType;
import com.ideaas.ecomm.ecomm.payload.AFIP.Person;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("all")
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

    private Double subtotal;

    private Double total;

    private String comments;

    private List<Item> items;

}
