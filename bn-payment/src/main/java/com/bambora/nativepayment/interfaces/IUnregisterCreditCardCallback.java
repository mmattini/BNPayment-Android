package com.bambora.nativepayment.interfaces;

/**
 * {@link IUnregisterCreditCardCallback} is an interface defining the the callback method
 * for a credit card unregister operation.
 *
 * Created by oskarhenriksson on 16/10/15.
 */
public interface IUnregisterCreditCardCallback {

    /**
     * A callback method executed after a credit card unregister operation is finished.
     *
     * @param success A boolean indicating the result of the operation.
     */
    void onUnregisterCreditCardFinished(boolean success);

}
