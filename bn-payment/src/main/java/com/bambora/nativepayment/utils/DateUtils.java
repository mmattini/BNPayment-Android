package com.bambora.nativepayment.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This is a class containing utils regarding Dates
 *
 * Created by oskarhenriksson on 14/10/15.
 */
public class DateUtils {

    private DateUtils() {

    }

    /**
     * This is a helper method for converting a Date to a String that can be used
     * in the date http header field. Currently used for generating an auth header.
     *
     * @param date The date to be formatted to a string
     * @return A String representation of a date that can be used as a date http header.
     */
    public static String getDateHeader(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String dateHeader = dateFormat.format(date);
        dateHeader += " GMT";
        return dateHeader;
    }
}
