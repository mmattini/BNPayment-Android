/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.handlers;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bambora.nativepayment.base.MockitoInstrumentationTestCase;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.network.HttpClient;
import com.bambora.nativepayment.network.Request;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;

/**
 * Instrumented tests for the {@link BNPaymentHandler} class.
 */
@RunWith(AndroidJUnit4.class)
public class BNPaymentHandlerTest extends MockitoInstrumentationTestCase {

    @Before
    @Override
    public void setUp() {
        super.setUp();
        BNPaymentHandler bnPaymentHandler = BNPaymentHandler.getInstance();
        bnPaymentHandler.clearSetup();
        bnPaymentHandler.setCertificateManager(Mockito.mock(CertificateManager.class));
    }

    @Test
    public void shouldGetAPIToken() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getContext(), "token");

        // When
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        // Then
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        Assert.assertEquals("token", instance.getApiToken());
    }

    /**
     * Verify that Api-Token (but not Merchant-Account) header is set
     */
    @Test
    public void shouldSetAPITokenHeader() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getContext(), "token");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        HttpClient httpClient = instance.getHttpClient();

        Request request = new Request(null, null);

        // When
        httpClient.addDefaultHeaders(request);

        // Then
        Assert.assertNull(request.getHeader().get("Merchant-Account"));
        Assert.assertNotNull(request.getHeader().get("Api-Token"));
        Assert.assertEquals("token", request.getHeader().get("Api-Token").get(0));
    }

    /**
     * Verify that merchant account can be set on {@link com.bambora.nativepayment.handlers.BNPaymentHandler}
     */
    @Test
    public void shouldSetMerchantAccountString() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getContext());
        bnPaymentBuilder.merchantAccount("merchant");

        // When
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();

        // Then
        Assert.assertEquals("merchant", instance.getMerchantAccount());
    }

    /**
     * Verify that Merchant-Account (but not Api-Token) header is set
     */
    @Test
    public void shouldGetMerchantAccountHeader() {
        // Given
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getContext());
        bnPaymentBuilder.merchantAccount("merchant");

        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        HttpClient httpClient = instance.getHttpClient();

        Request request = new Request(null, null);

        // When
        httpClient.addDefaultHeaders(request);

        // Then
        Assert.assertNull(request.getHeader().get("Api-Token"));
        Assert.assertNotNull(request.getHeader().get("Merchant-Account"));
        Assert.assertEquals("merchant", request.getHeader().get("Merchant-Account").get(0));
    }

    @Test
    public void shouldSetBaseURL() throws IllegalAccessException, NoSuchFieldException {
        // Given
        String expectedValue = "test";

        // When
        BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getContext());
        bnPaymentBuilder.baseUrl("test");

        // Then
        Class bnPaymentBuilderClass = bnPaymentBuilder.getClass();

        Field baseUrlField = bnPaymentBuilderClass.getDeclaredField("baseUrl");
        baseUrlField.setAccessible(true);

        String actualValue = (String) baseUrlField.get(bnPaymentBuilder);

        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void shouldReturnABNPaymentHandlerInstance() {
        // When
        BNPaymentHandler instance = BNPaymentHandler.getInstance();
        BNPaymentHandler anotherInstance = BNPaymentHandler.getInstance();

        int identityHashCodeOfInstance = System.identityHashCode(instance);
        int identityHashCodeOfAnotherInstance = System.identityHashCode(anotherInstance);

        // Then
        Assert.assertEquals(BNPaymentHandler.class, instance.getClass());
        Assert.assertEquals(identityHashCodeOfInstance, identityHashCodeOfAnotherInstance);
    }
}
