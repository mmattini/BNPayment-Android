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

package com.bambora.nativepayment.security;

import com.bambora.nativepayment.utils.CertificateUtils;

import org.junit.Assert;
import org.junit.Test;

import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Arrays;

/**
 * Local tests for the {@link CertificateUtils} class.
 */
public class CertificateUtilsLocalTest {

    @Test
    public void parsedCertificateStringShouldMatchCertfile() throws Exception {
        //When
        Certificate certificate = CertificateUtils.parseCertificate(LocalTestData.getCertificateFromString());

        // Then
        Assert.assertNotNull(certificate);

        // When
        PublicKey publicKeyFromString = certificate.getPublicKey();
        PublicKey publicKeyFromKeyStore = LocalTestData.getKeyPairFromRSAKeystore(getClass().getClassLoader()).getPublic();

        // Then
        Assert.assertTrue(Arrays.equals(publicKeyFromString.getEncoded(), publicKeyFromKeyStore.getEncoded()));
    }
}
