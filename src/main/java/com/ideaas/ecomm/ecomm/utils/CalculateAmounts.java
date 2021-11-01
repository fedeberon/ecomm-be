package com.ideaas.ecomm.ecomm.utils;

import com.ideaas.ecomm.ecomm.enums.IVAConditionType;

public class CalculateAmounts {


    public static Double calculateIVAAmount(final IVAConditionType conditionType,
                                            final Double amount) {

        return (amount * conditionType.getValue()) / 100;
    }

}
