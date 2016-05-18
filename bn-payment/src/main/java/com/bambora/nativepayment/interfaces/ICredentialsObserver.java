package com.bambora.nativepayment.interfaces;


import com.bambora.nativepayment.network.RequestError;

/**
 * An interface defining a callback for then Credentials are loaded from disk.
 */
public interface ICredentialsObserver {
    /**
     * A method indicating the result of an operation loading credentials.
     *
     * @param success A boolean indicating the result of the operation.
     */
    void onCredentialsLoaded();
    void onCredentialsError(RequestError error);
}
