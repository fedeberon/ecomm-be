package com.ideaas.ecomm.ecomm.utils;

import com.ideaas.ecomm.ecomm.enums.IVAConditionType;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculateAmountsTest {


    @Test
    public void shouldReturnTwentyOnePercentOfAmount() {
        final Double amount = 100.0;
        final IVAConditionType twentyOnePercent = IVAConditionType.VEINTIUNO_PORCIENTO;
        final Double result = 21.0;

        assertEquals(result, CalculateAmounts.calculateIVAAmount(twentyOnePercent, amount));
    }

    @Test
    public void shouldReturnTwentySevenPercentOfAmount() {
        final Double amount = 100.0;
        final IVAConditionType twentySevenPercent = IVAConditionType.VEINTISIETE_PORCIENTO;
        final Double result = 27.0;

        assertEquals(result, CalculateAmounts.calculateIVAAmount(twentySevenPercent, amount));
    }


}
