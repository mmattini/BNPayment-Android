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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;

/**
 * Instrumented tests for the {@link JsonContainer} class.
 */
@RunWith(AndroidJUnit4.class)
public class JSONContainerTest {

    @Test
    public void shouldGetJSONObject() throws IllegalAccessException, JSONException, NoSuchFieldException {
        // Given
        JSONObject expectedJsonObject = new JSONObject();
        expectedJsonObject.put("Key", "Value");
        JsonContainer jsonContainer = new JsonContainer(expectedJsonObject);

        // When
        JSONObject retrievedJSONObject = jsonContainer.getJsonObject();

        Class jsonContainerClass = jsonContainer.getClass();
        Field jsonField = jsonContainerClass.getDeclaredField("json");
        jsonField.setAccessible(true);
        Object jsonProperty = (Object) jsonField.get(jsonContainer);

        // Then
        Assert.assertTrue(jsonProperty instanceof JSONObject);
        Assert.assertEquals(expectedJsonObject, retrievedJSONObject);
    }

    @Test(expected = JSONException.class)
    public void shouldNotGetAJSONObject() throws JSONException {
        // Given
        JsonContainer jsonContainer = new JsonContainer("Not JSON");

        // When
        jsonContainer.getJsonObject();
    }

    @Test
    public void shouldGetAJSONArray() throws IllegalAccessException, JSONException, NoSuchFieldException {
        // Given
        String arrayContent = "[1,2,3]";
        JSONArray expectedJsonArray = new JSONArray(arrayContent);
        JsonContainer jsonContainer = new JsonContainer(expectedJsonArray);

        // When
        JSONArray retrievedJSArray = jsonContainer.getJsonArray();

        Class jsonContainerClass = jsonContainer.getClass();
        Field jsonField = jsonContainerClass.getDeclaredField("json");
        jsonField.setAccessible(true);
        Object jsonProperty = (Object) jsonField.get(jsonContainer);

        // Then
        Assert.assertTrue(jsonProperty instanceof JSONArray);
        Assert.assertEquals(expectedJsonArray, retrievedJSArray);
    }

    @Test(expected = JSONException.class)
    public void shouldNotGetAJSONArray() throws JSONException {
        // Given
        JsonContainer jsonContainer = new JsonContainer("Not JSON");

        // When
        jsonContainer.getJsonArray();
    }


    @Test
    public void returnValueShouldBeAJSONObject() throws JSONException {
        // Given
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key", "Value");

        // When
        JsonContainer jsonContainer = new JsonContainer(jsonObject);

        // Then
        Assert.assertTrue(jsonContainer.isJsonObject());
    }

    @Test
    public void returnValueShouldNotBeAJSONObject() throws JSONException {
        // When
        JsonContainer jsonContainer = new JsonContainer("jsonstring");

        // Then
        Assert.assertFalse(jsonContainer.isJsonObject());
    }

    @Test
    public void returnValueShouldBeAJSONArray() throws JSONException {
        // Given
        String arrayContent = "[1,2,3]";
        JSONArray expectedJsonArray = new JSONArray(arrayContent);

        // When
        JsonContainer jsonContainer = new JsonContainer(expectedJsonArray);

        // Then
        Assert.assertTrue(jsonContainer.isJsonArray());
    }

    @Test
    public void returnValueShouldNotBeAJSONArray() throws JSONException {
        // When
        JsonContainer jsonContainer = new JsonContainer("Not a JSON Array");

        // Then
        Assert.assertFalse(jsonContainer.isJsonArray());
    }
}
