package com.bambora.nativepayment.interfaces;

/**
 * {@link ICreditCardRegistrationStatusCallback} is an interface defining the the callback method
 * for a credit card registration operation.
 *
 * Created by oskarhenriksson on 30/10/15.
 */
public interface ICreditCardRegistrationStatusCallback {

    /**
     * A callback method executed after a credit card registration operation is finished.
     *
     * @param registered A boolean indicating the result of the operation.
     */
    void onInitiateCreditCardRegistrationFinished(boolean registered);

}
