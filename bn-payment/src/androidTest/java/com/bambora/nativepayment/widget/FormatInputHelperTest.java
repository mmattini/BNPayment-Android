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

package com.bambora.nativepayment.widget;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for the {@link FormInputHelper} class.
 */
@RunWith(AndroidJUnit4.class)
public class FormatInputHelperTest {

    @Test
    public void nonDigitsShouldBeDeleted() {
        // Given
        String expectedString = "1234567890";

        // When
        String stringToEdit = FormInputHelper.clearNonDigits("-.,§?/()]≈[abcdefghijk1234567890lmnopqrstuvqyzåäö");

        // Then
        Assert.assertEquals(expectedString, stringToEdit);
    }

    @Test
    public void charactersOutsideSpecifiedBoundsShouldBeDeleted() {
        // Given
        CharSequence characterSequence = "0123456789Test-.,";
        String expectedString = "0123456789";

        // When
        String result = FormInputHelper.getDeletedChars(characterSequence, 0, 10);

        // Then
        Assert.assertEquals(expectedString, result);
    }

    @Test
    public void shouldCreateObject() {
        // When
        FormInputHelper formInputHelperObject = new FormInputHelper();

        // Then
        Assert.assertEquals(FormInputHelper.class, formInputHelperObject.getClass());
    }
}
