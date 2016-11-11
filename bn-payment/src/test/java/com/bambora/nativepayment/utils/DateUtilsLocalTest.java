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

import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Local tests for the {@link DateUtils} class.
 */
public class DateUtilsLocalTest {

    @Test
    public void dateHeaderShouldHaveCorrectFormat() {
        long timestamp = 1433160000;
        String correctFormat = "Mon, 01 Jun 2015 12:00:00 GMT";
        String dateStringToTest = DateUtils.getDateHeader(new Date(timestamp*1000));

        Assert.assertEquals(correctFormat, dateStringToTest);
    }

    @Test
    public void shouldReturnTheCorrectTime() throws ParseException {
        Map<String, Long> timeTestValues = new HashMap<>();
        timeTestValues.put("2016-05-17T12:00:00Z", 1463486400000L); // String to parse, time value
        timeTestValues.put("2016-05-17T12:00:00+01:00", 1463482800000L);
        timeTestValues.put("2016-05-17T12:00:00+0100", 1463482800000L);

        for (String dateString : timeTestValues.keySet()) {
            Date parsedDate = DateUtils.parseISO8601DateString(dateString);
            long expectedTime = timeTestValues.get(dateString);
            Assert.assertEquals(expectedTime, parsedDate.getTime());
        }
    }
}
