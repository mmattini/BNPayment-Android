package com.bambora.nativepayment.network;

import com.bambora.nativepayment.interfaces.IJsonResponse;

/**
 * Interface for notifying about network request result.
 */
public interface Callback<T extends IJsonResponse<T>> {
    void onSuccess(Response<T> response);
    void onError(RequestError error);
}
