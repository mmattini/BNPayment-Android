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

package com.bambora.nativepayment.mock;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.webview.ICreditCardRegistrationView;

/**
 * @author Lovisa Corp
 */
public class CreditCardRegistrationViewMock implements ICreditCardRegistrationView {

    private boolean onNotifyRegistrationStartedCalled;
    private boolean onNotifySuccessCalled;
    private boolean onNotifyFailureCalled;
    private RegistrationFormError registrationFormError;
    private CreditCard registeredCard;

    @Override
    public void loadRegistrationUrl(String url) {
        // Never called in tests
    }

    @Override
    public void notifyRegistrationStarted() {
        onNotifyRegistrationStartedCalled = true;
    }

    @Override
    public void notifyRegistrationSuccess(CreditCard creditCard) {
        onNotifySuccessCalled = true;
        registeredCard = creditCard;
    }

    @Override
    public void notifyError(RegistrationFormError error) {
        onNotifyFailureCalled = true;
        this.registrationFormError = error;
    }

    public boolean wasOnNotifyRegistrationStartedCalled() {
        return onNotifyRegistrationStartedCalled;
    }

    public boolean wasOnNotifySuccessCalled() {
        return onNotifySuccessCalled;
    }

    public boolean wasOnNotifyFailureCalled() {
        return onNotifyFailureCalled;
    }

    public RegistrationFormError getRegistrationFormError() {
        return registrationFormError;
    }

    public CreditCard getRegisteredCard() {
        return registeredCard;
    }
}
