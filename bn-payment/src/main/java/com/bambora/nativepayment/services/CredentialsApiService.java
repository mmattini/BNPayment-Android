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

package com.bambora.nativepayment.services;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.interfaces.IGetCredentialsCallback;
import com.bambora.nativepayment.models.Credentials;
import com.bambora.nativepayment.models.UserModel;
import com.bambora.nativepayment.network.ApiService;
import com.bambora.nativepayment.network.Callback;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.network.Response;

import static com.bambora.nativepayment.network.RequestMethod.POST;


/**
 * API service class for defining all requests handling credentials.
 */
public class CredentialsApiService extends ApiService {

    /**
     * Creates a request for fetching a new set of credentials from Bambora Native payments API.
     *
     * @param userModel Model of the user to be registered
     * @return A {@link Request} object
     */
    public Request<Credentials> getCredentials(UserModel userModel) {
        return new Request<>(this, Credentials.class)
                .endpoint("credentials/")
                .method(POST)
                .body(userModel)
                .requireAuthenticator(false);
    }

    private static CredentialsApiService createService() {
        return BNPaymentHandler.getInstance().createService(CredentialsApiService.class);
    }

    /**
     * Helper class for executing the API requests.
     */
    public static class CredentialsService {

        /**
         * Fetches {@link Credentials} to be used for authenticating the app in future API requests.
         *
         * @param userModel A model representing an User
         * @param callback  A result listener
         */
        public static void getCredentials(UserModel userModel, final IGetCredentialsCallback callback) {
            createService().getCredentials(userModel).execute(new Callback<Credentials>() {
                @Override
                public void onSuccess(Response<Credentials> response) {
                    callback.onCredentialsReceived(response.getBody());
                }

                @Override
                public void onError(RequestError error) {
                    callback.onError(error);
                }
            });
        }
    }
}
