package com.bambora.nativepayment.services;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.interfaces.ITransactionListener;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.TransactionResponse;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.network.ApiService;
import com.bambora.nativepayment.network.Callback;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.models.creditcard.RegistrationFormSettings;
import com.bambora.nativepayment.models.creditcard.RegistrationResponse;
import com.bambora.nativepayment.network.RequestError;
import com.bambora.nativepayment.network.Response;

import static com.bambora.nativepayment.network.RequestMethod.POST;

/**
 * An {@link ApiService} class for setting up credit card related API endpoints/requests.
 */
public class PaymentApiService extends ApiService {

    /**
     * Creates a {@link Request} for initiating a hosted payment page to be used for registering
     * a credit card for future payments.
     *
     * @param formSettings  {@link RegistrationFormSettings} for customizing the form
     * @return A URL to the initiated page
     */
    public Request<RegistrationResponse> initiateHpp(RegistrationFormSettings formSettings) {
        return new Request<>(this, RegistrationResponse.class)
                .endpoint("hpp/")
                .method(POST)
                .body(formSettings);
    }

    /**
     * Creates a {@link Request} for making a transaction with given settings.
     *
     * @param paymentId         A unique identifier for the payment
     * @param paymentSettings   {@link PaymentSettings}
     * @return A {@link Request} to be executed
     */
    public Request<TransactionResponse> makeTransaction(String paymentId, PaymentSettings paymentSettings) {
        return new Request<>(this, TransactionResponse.class)
                .endpoint("payments/{paymentId}/card_token/")
                .endpointParameter("paymentId", paymentId)
                .method(POST)
                .body(paymentSettings);
    }

    private static PaymentApiService createService() {
        return BNPaymentHandler.getInstance().createService(PaymentApiService.class);
    }

    public interface IHppResultListener {
        void onHppInitiated(String url);
        void onError(RegistrationFormError registrationFormError);
    }

    /**
     * Helper class for executing the API requests.
     */
    public static class PaymentService {

        public static Request initiateHppRegistration(RegistrationFormSettings formSettings, final IHppResultListener resultListener) {
            Request<RegistrationResponse> request = createService().initiateHpp(formSettings);
            request.execute(new Callback<RegistrationResponse>() {
                @Override
                public void onSuccess(Response<RegistrationResponse> response) {
                    String url = response.getBody().sessionUrl;
                    if (resultListener != null) {
                        if (url != null) {
                            resultListener.onHppInitiated(url);
                        } else {
                            BNLog.e(getClass().getSimpleName(), "Credit card registration failed to initiate. URL is null.");
                            resultListener.onError(RegistrationFormError.PAGE_LOAD_ERROR);
                        }
                    }
                }

                @Override
                public void onError(RequestError error) {
                    if (resultListener != null) {
                        resultListener.onError(RegistrationFormError.PAGE_LOAD_ERROR);
                    }
                }
            });
            return request;
        }

        /**
         * A method for making a transaction.
         *
         * @param paymentSettings {@link PaymentSettings} containing transaction details.
         * @param listener A callback that returns {@link ITransactionListener.TransactionResult}
         *                 indicating the result of the transaction.
         */
        public static Request makeTransaction(String paymentId, PaymentSettings paymentSettings, final ITransactionListener listener) {
            Request<TransactionResponse> request = createService().makeTransaction(paymentId, paymentSettings);
            request.execute(new Callback<TransactionResponse>() {
                @Override
                public void onSuccess(Response<TransactionResponse> response) {
                    if (listener != null) {
                        listener.onTransactionResult(ITransactionListener.TransactionResult.TRANSACTION_RESULT_SUCCESS);
                    }
                }

                @Override
                public void onError(RequestError error) {
                    if (listener != null) {
                        listener.onTransactionResult(ITransactionListener.TransactionResult.TRANSACTION_RESULT_FAILURE);
                    }
                }
            });
            return request;
        }
    }
}
