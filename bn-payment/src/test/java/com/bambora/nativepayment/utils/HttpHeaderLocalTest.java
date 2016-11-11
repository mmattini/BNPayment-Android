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

import com.bambora.nativepayment.network.HttpHeader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Local test for the {@link HttpHeader} class.
 */
public class HttpHeaderLocalTest {

    private HttpHeader header;

    @Before
    public void setup() {
        header = new HttpHeader();
    }

    @Test
    public void testAddField() {
        String key1 = "test-key";
        String key2 = "another-test-key";
        String value1 = "test-value";
        String value2 = "test-value-2";
        String value3 = "another-test-value";

        header.addField(key1, value1);
        Assert.assertEquals(1, header.size());
        Assert.assertEquals("\t\ttest-key: test-value, \n", header.toString());

        // Try with a duplicate value
        header.addField(key1, value1);
        Assert.assertEquals(1, header.size());
        Assert.assertEquals("\t\ttest-key: test-value, \n", header.toString());

        // Add a new value to an existing key
        header.addField(key1, value2);
        Assert.assertEquals(1, header.size());
        Assert.assertEquals("\t\ttest-key: test-value, test-value-2, \n", header.toString());

        // New key, new value
        header.addField(key2, value3);
        Assert.assertEquals(2, header.size());
    }
}
