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
     * Creates a request for fetching a new set of credentials from Bambora Native payments.
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
