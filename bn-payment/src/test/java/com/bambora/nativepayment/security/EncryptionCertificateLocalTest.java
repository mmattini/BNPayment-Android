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

import com.bambora.nativepayment.utils.DateUtils;

import org.junit.Assert;
import org.junit.Test;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Local tests for the {@link EncryptionCertificate} class.
 */
public class EncryptionCertificateLocalTest {

    private List<Certificate> masterCertificates;

    public EncryptionCertificateLocalTest() throws CertificateException {
        masterCertificates = new ArrayList<>();
        masterCertificates.add(LocalTestData.getValidMasterCertificate(getClass().getClassLoader()));
    }

    @Test
    public void certificateShouldBeTrusted() throws CertificateException {
        // Given
        Certificate certificate = LocalTestData.getValidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};

        // When
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, null, 0);

        // Then
        Assert.assertTrue(encryptionCertificate.isChainTrusted(masterCertificates));
    }

    @Test
    public void certificateShouldNotBeTrusted() throws CertificateException {
        // Given
        Certificate certificate = LocalTestData.getInvalidEncryptionCertificate(getClass().getClassLoader());
        X509Certificate[] chain = {(X509Certificate) certificate};

        // When
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, null, 0);

        // Then
        Assert.assertFalse(encryptionCertificate.isChainTrusted(masterCertificates));
    }

    @Test
    public void certificateShouldNotHaveExpired() throws CertificateException {
        // Given
        Certificate certificate = LocalTestData.getValidEncryptionCertificate(getClass().getClassLoader());
        Date now = new Date();
        Date inTenDays = DateUtils.addDays(now, 10);
        int updateInterval = 5;
        X509Certificate[] chain = {(X509Certificate) certificate};

        // When
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, inTenDays, updateInterval);

        // Then
        Assert.assertFalse(encryptionCertificate.isCloseToExpiry());
    }

    @Test
    public void certificateShouldHaveExpired() throws CertificateException {
        // Given
        Certificate certificate = LocalTestData.getValidEncryptionCertificate(getClass().getClassLoader());
        Date now = new Date();
        Date yesterday = DateUtils.addDays(now, -1);
        int updateInterval = 0;
        X509Certificate[] chain = {(X509Certificate) certificate};

        // When
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, yesterday, updateInterval);

        // Then
        Assert.assertTrue(encryptionCertificate.isCloseToExpiry());
    }

    @Test
    public void certificateShouldHaveAlmostExpired() throws CertificateException {
        // Given
        Certificate certificate = LocalTestData.getValidEncryptionCertificate(getClass().getClassLoader());
        Date now = new Date();
        Date inFiveDays = DateUtils.addDays(now, 5);
        int updateInterval = 6;
        X509Certificate[] chain = {(X509Certificate) certificate};

        // When
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, chain, inFiveDays, updateInterval);

        // Then
        Assert.assertTrue(encryptionCertificate.isCloseToExpiry());
    }

    @Test
    public void certificateChainShouldBeTrusted() throws Exception {
        // Given
        X509Certificate[] certificateChain = LocalTestData.getValidCertificateChain(getClass().getClassLoader());

        // When
        EncryptionCertificate encryptionCertificate = new EncryptionCertificate(null, certificateChain, null, 0);

        // Then
        Assert.assertTrue(encryptionCertificate.isChainTrusted(masterCertificates));
    }
}
