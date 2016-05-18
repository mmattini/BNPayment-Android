package com.bambora.nativepayment.utils;

import org.junit.Assert;
import org.junit.Test;

import java.security.GeneralSecurityException;

/**
 * Created by oskarhenriksson on 11/12/15.
 */
public class HashUtilsTest {

    @Test
    public void testGenerateHmac() throws GeneralSecurityException {
        String hmac = HashUtils.generateHmac("HmacSHA256", "Message to hmac", "Key");
        Assert.assertEquals(hmac, hmac);
    }

    @Test
    public void testGeneratePow() {
        Assert.assertEquals("a", "a");
    }
}