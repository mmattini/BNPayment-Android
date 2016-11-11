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

package com.bambora.nativepayment.json;

import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * Instrumented tests for the {@link BNJsonObject} class.
 */
@RunWith(AndroidJUnit4.class)
public class BNJsonObjectTest {

    @Test
    public void testOptDateWithValidDate() throws JSONException {
        // Given
        long expectedTime = 1463486400000L;
        String dateKey = "date";
        String dateValue = "2016-05-17T12:00:00Z";
        String json = createJsonString(dateKey, dateValue);
        BNJsonObject jsonObject = new BNJsonObject(json);

        // When
        Date date = jsonObject.optDate(dateKey);

        // Then
        Assert.assertNotNull(date);
        Assert.assertEquals(expectedTime, date.getTime());
    }

    @Test
    public void shouldReturnEmptyDateWhenNoDateIsSpecified() throws JSONException {
        // Given
        String dateKey = "date";
        String dateValue = "null";
        String json = createJsonString(dateKey, dateValue);
        BNJsonObject jsonObject = new BNJsonObject(json);

        // When
        Date date = jsonObject.optDate(dateKey);

        // Then
        Assert.assertNull(date);
    }

    @Test
    public void shouldReturnEmptyDateWhenInvalidKeyIsUsed() throws JSONException {
        // Given
        BNJsonObject jsonObject = new BNJsonObject("{}");

        // When
        Date date = jsonObject.optDate("invalidKey");

        // Then
        Assert.assertNull(date);
    }

    private String createJsonString(String key, String value) {
        String jsonString = "{\"<key>\":\"<value>\"}";
        return jsonString
                .replace("<key>", key)
                .replace("<value>", value);
    }
}
