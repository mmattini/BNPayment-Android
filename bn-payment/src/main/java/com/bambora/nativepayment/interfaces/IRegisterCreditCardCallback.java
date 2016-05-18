package com.bambora.nativepayment.interfaces;

/**
 * {@link IRegisterCreditCardCallback} is an interface defining the the callback method
 * for a credit card registration initialization operation.
 *
 * Created by oskarhenriksson on 15/10/15.
 */
public interface IRegisterCreditCardCallback {

    /**
     * A callback method executed after a credit card registration initialization operation is finished.
     *
     * @param url A String containing an URL to be opened in a webview.
     */
    void onInitiateCreditCardRegistrationFinished(String url);
    void onError(Throwable throwable);
}
