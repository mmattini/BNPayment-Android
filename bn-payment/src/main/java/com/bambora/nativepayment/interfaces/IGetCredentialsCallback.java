package com.bambora.nativepayment.interfaces;

import com.bambora.nativepayment.models.Credentials;
import com.bambora.nativepayment.network.RequestError;

/**
 * Interface for credentials loading callbacks.
 */
public interface IGetCredentialsCallback {
    void onCredentialsReceived(Credentials credentials);
    void onError(RequestError error);
}
