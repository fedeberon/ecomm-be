package com.ideaas.ecomm.ecomm.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutResponse {

    private List<CheckoutResponseDetail> detailList;

}
