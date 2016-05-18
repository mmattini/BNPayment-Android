package com.bambora.nativepayment.handlers;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by oskarhenriksson on 29/02/16.
 */
public class BNPaymentsHandlerTest {

    @Test
    public void testGetInstance()  {
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        Assert.assertNotNull(instance);
    }

}