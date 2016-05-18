package com.bambora.nativepayment.logging;

import android.util.Log;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.network.Response;

/**
 * This is a helper class to handle logging for the Bambora Native SDK. It wraps some logging
 * through {@link Log} but only logs in debug mode.
 */
public class BNLog {

    private static final String DIVIDER = "--------------------------------------\n";

    public static void d(String tag, String msg) {
        if (debug()) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (debug()) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (debug()) {
            Log.e(tag, msg, throwable);
        }
    }

    public static boolean debug() {
        return BNPaymentHandler.getInstance().getDebug();
    }

    public static void requestResult(String logTag, Request request, Response response) {
        boolean success = response != null && response.error == null;
        String title = success ?
                "BN request completed successfully\n" :
                "BN request failed\n";
        StringBuilder logMessage = new StringBuilder();
        logMessage
                .append(DIVIDER)
                .append(title)
                .append(DIVIDER);
        if (response != null) {
            logMessage.append(response.toString());
        }
        if (request != null) {
            logMessage
                    .append(DIVIDER)
                    .append("Original request\n")
                    .append(DIVIDER)
                    .append(request.toString());
        }
        logMessage.append(DIVIDER);
        if (success) {
            d(logTag, logMessage.toString());
        } else {
            e(logTag, logMessage.toString());
        }
    }

    public static void jsonParseError(String logTag, Exception exception) {
        e(logTag, "Failed to parse JSON string.", exception);
    }
}
