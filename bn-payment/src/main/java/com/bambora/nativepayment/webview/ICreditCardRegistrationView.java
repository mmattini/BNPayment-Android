package com.bambora.nativepayment.webview;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;

/**
 * @author Lovisa Corp
 */
public interface ICreditCardRegistrationView {
    void loadRegistrationUrl(String url);
    void notifyRegistrationStarted();
    void notifyRegistrationSuccess(CreditCard creditCard);
    void notifyError(RegistrationFormError error);
}
