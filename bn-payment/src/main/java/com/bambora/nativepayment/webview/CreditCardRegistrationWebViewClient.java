package com.bambora.nativepayment.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * A WebViewClient class that implements onPageFinished in order to notify
 * {@link IOnPageFinishedListener} when the WebView's URL completed loading.
 */
public class CreditCardRegistrationWebViewClient extends WebViewClient {

    private IOnPageFinishedListener onPageFinishedListener;

    /**
     * Interface used for notifying when the WebView has finished loading it's page.
     */
    public interface IOnPageFinishedListener {
        void onPageFinished();
    }

    public CreditCardRegistrationWebViewClient(IOnPageFinishedListener onPageFinishedListener) {
        this.onPageFinishedListener = onPageFinishedListener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (onPageFinishedListener != null) {
            onPageFinishedListener.onPageFinished();
        }
    }
}
