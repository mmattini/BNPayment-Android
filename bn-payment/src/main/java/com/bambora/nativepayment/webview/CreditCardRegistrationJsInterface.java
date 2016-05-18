package com.bambora.nativepayment.webview;

import android.webkit.JavascriptInterface;

import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.creditcard.RegistrationResult;

import org.json.JSONException;

/**
 * Class for JavaScript callback interface handling credit card registration status.
 * @author Lovisa Corp
 */
public class CreditCardRegistrationJsInterface {

    private IJavascriptCallbackListener resultListener;

    public interface IJavascriptCallbackListener {
        void onJavascriptCall(RegistrationResult result);
    }

    public CreditCardRegistrationJsInterface(IJavascriptCallbackListener resultListener) {
        this.resultListener = resultListener;
    }

    /**
     * Gets called by credit card registration page when registration state changes.
     * @param stateChangeJson Json String describing a {@link RegistrationResult}
     */
    @JavascriptInterface
    public void paymentFormStatusChanged(String stateChangeJson) {
        try {
            RegistrationResult result = RegistrationResult.fromJson(stateChangeJson);
            resultListener.onJavascriptCall(result);
        } catch (JSONException e) {
            BNLog.e(getClass().getSimpleName(), "Failed to parse Javascript JSON callback.", e);
            resultListener.onJavascriptCall(null);
        }
    }
}
