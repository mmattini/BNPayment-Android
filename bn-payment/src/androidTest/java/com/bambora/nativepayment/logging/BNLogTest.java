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

package com.bambora.nativepayment.logging;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.bambora.nativepayment.base.MockitoInstrumentationTestCase;
import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.managers.CertificateManager;
import com.bambora.nativepayment.models.CertificatesResponse;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.network.Response;
import com.bambora.nativepayment.services.CertificateApiService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Instrumented tests for the {@link BNLog} class.
 */
@RunWith(AndroidJUnit4.class)
public class BNLogTest extends MockitoInstrumentationTestCase {

    private BNPaymentHandler.BNPaymentBuilder bnPaymentBuilder;

    @Before
    @Override
    public void setUp() {
        super.setUp();

        BNPaymentHandler bnPaymentHandler = BNPaymentHandler.getInstance();
        bnPaymentHandler.clearSetup();
        bnPaymentHandler.setCertificateManager(Mockito.mock(CertificateManager.class));

        bnPaymentBuilder = new BNPaymentHandler.BNPaymentBuilder(
                InstrumentationRegistry.getContext());
    }

    @Test
    public void testDWithoutDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(false);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        // When
        BNLog.d("Tag", "Message");
    }

    @Test
    public void testDWithDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(true);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        // When
        BNLog.d("Tag", "Message");
    }

    @Test
    public void testEWithoutDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(false);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        // When
        BNLog.e("Tag", "Message");
    }

    @Test
    public void testEWithDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(true);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);

        // When
        BNLog.e("Tag", "Message");
    }

    @Test
    public void testEThrowableWithoutDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(false);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        Exception exception = new Exception();

        // When
        BNLog.e("Tag", "Message", exception);
    }

    @Test
    public void testEThrowableWithDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(true);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        Exception exception = new Exception();

        // When
        BNLog.e("Tag", "Message", exception);
    }

    @Test
    public void testWWithoutDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(false);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        Exception exception = new Exception();

        // When
        BNLog.w("Tag", "Message", exception);
    }

    @Test
    public void testWWithDebugEnabled() {
        // Given
        bnPaymentBuilder.debug(true);
        BNPaymentHandler.setupBNPayments(bnPaymentBuilder);
        Exception exception = new Exception();

        // When
        BNLog.w("Tag", "Message", exception);
    }

    @Test
    public void testObjectCreation() {
        // When
        BNLog bnLogObject = new BNLog();

        // Then
        Assert.assertEquals(BNLog.class, bnLogObject.getClass());
    }

    @Test
    public void testJsonParseError() {
        // Given
        Exception exception = new Exception();

        // When
        BNLog.jsonParseError("Tag", exception);
    }

    @Test
    public void testRequestResult() {
        // Given
        CertificateApiService certificateApiService = new CertificateApiService();
        Request request = new Request(certificateApiService, CertificatesResponse.class);
        Response response = new Response();
        response.error = null;

        // When
        BNLog.requestResult("Tag", request, response);
    }

    @Test
    public void testRequestResultNegative() {
        // Given
        Request request = null;
        Response response = null;

        // When
        BNLog.requestResult("Tag", request, response);
    }
}
