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

package com.bambora.nativepayment.utils;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.GeneralSecurityException;

/**
 * Instrumented tests for the {@link HashUtils} class.
 */
@RunWith(AndroidJUnit4.class)
public class HashUtilsTest {

    private static final String HASH_ALGORITHM = "HmacSHA256";
    private static final String TEST_KEY = "SecretKey";
    private static final String TEST_STRING_TO_HASH = "A message to hash.";
    private static final String EXPECTED_RESULT = "87276fcff3d5f8bf47359a1f61106e8605be6b1b9fa8ab047e75d01178068ab9";

    @Test
    public void shouldGenerateValidHmac() throws GeneralSecurityException {
        // When
        String hmac = HashUtils.generateHmac(HASH_ALGORITHM, TEST_STRING_TO_HASH, TEST_KEY);
        // Then
        Assert.assertEquals(EXPECTED_RESULT, hmac);
    }
}
