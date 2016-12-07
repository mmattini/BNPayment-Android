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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Instrumented tests for the {@link DateUtils} class.
 */
@RunWith(AndroidJUnit4.class)
public class DateUtilsTest {

    @Test
    public void shouldReturnTheCorrectTime() throws ParseException {
        // Given
        Map<String, Long> timeTestValues = new HashMap<>();
        timeTestValues.put("2016-05-17T12:00:00Z", 1463486400000L); // String to parse, time value
        timeTestValues.put("2016-05-17T12:00:00+01:00", 1463482800000L);
        timeTestValues.put("2016-05-17T12:00:00+0100", 1463482800000L);

        for (String dateString : timeTestValues.keySet()) {

            // When
            Date parsedDate = DateUtils.parseISO8601DateString(dateString);
            long expectedTime = timeTestValues.get(dateString);

            // Then
            Assert.assertEquals(expectedTime, parsedDate.getTime());
        }
    }

    @Test
    public void shouldReturnTheCorrectDifferenceInDays() throws ParseException {
        // Given
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int expectedResult = 31;

        // When
        Date firstDateObject = simpleDateFormat.parse("2017-01-01");
        Date secondDateObject = simpleDateFormat.parse("2017-02-01");
        int result = DateUtils.getDifferenceInDays(firstDateObject, secondDateObject);

        // Then
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void shouldReturnTheCorrectDate() throws ParseException {
        // Given
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dateObject = simpleDateFormat.parse("2017-01-01");
        Date expectedResult = simpleDateFormat.parse("2017-02-01");

        // When
        Date actualResult = DateUtils.addDays(dateObject, 31);

        // Then
        Assert.assertEquals(expectedResult, actualResult);
    }
}
