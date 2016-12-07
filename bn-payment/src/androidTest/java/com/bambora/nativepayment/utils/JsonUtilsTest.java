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

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for the {@link JsonUtils} class.
 */
@RunWith(AndroidJUnit4.class)
public class JsonUtilsTest {

    @Test
    public void shouldReturnString() throws JSONException {
        // Given
        String expectedString = "value";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", expectedString);

        // When
        String stringFromJSONobject = JsonUtils.getStringIfExists(jsonObject, "key");

        // Then
        Assert.assertEquals(expectedString, stringFromJSONobject);
    }

    @Test
    public void shouldReturnInteger() throws JSONException {
        // Given
        Integer expectedInt = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", expectedInt);

        // When
        Integer intFromJSONobject = JsonUtils.getIntIfExists(jsonObject, "key");

        // Then
        Assert.assertEquals(expectedInt, intFromJSONobject);
    }

    @Test
    public void shouldReturnObject() throws JSONException {
        // Given
        JSONObject jsonSubObject = new JSONObject();
        jsonSubObject.put("SubObjectKey", "SubObjectValue");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", jsonSubObject);

        // When
        JSONObject jsonObjectFromJSONobject = JsonUtils.getJSONObjectIfExists(jsonObject, "key");

        // Then
        Assert.assertEquals(jsonSubObject, jsonObjectFromJSONobject);
    }

    @Test
    public void shouldCreateObject() {
        // When
        JsonUtils jsonUtilsObject = new JsonUtils();

        // Then
        Assert.assertEquals(JsonUtils.class, jsonUtilsObject.getClass());
    }
}
