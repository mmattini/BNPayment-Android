package com.bambora.nativepayment.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by oskarhenriksson on 27/11/15.
 */
public class DateUtilsTest {

    @Test
    public void dateHeaderShouldHaveCorrectFormat() {
        long timestamp = 1433160000;
        String correctFormat = "Mon, 01 Jun 2015 12:00:00 GMT";
        String dateStringToTest = DateUtils.getDateHeader(new Date(timestamp*1000));

        Assert.assertEquals(correctFormat, dateStringToTest);
    }

}