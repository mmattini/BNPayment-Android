package com.bambora.nativepayment.handlers;

import android.content.Context;

import com.bambora.nativepayment.interfaces.ITransactionListener;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.creditcard.RegistrationFormSettings;
import com.bambora.nativepayment.network.ApiService;
import com.bambora.nativepayment.network.BNHttpClient;
import com.bambora.nativepayment.network.Request;
import com.bambora.nativepayment.services.PaymentApiService;
import com.bambora.nativepayment.services.PaymentApiService.PaymentService;

import java.lang.reflect.Constructor;

/**
 * {@link BNPaymentHandler} handles the responsibilities of managing the authentication and
 * service creation. <br><br>
 *
 * Created by oskarhenriksson on 14/10/15.
 */

public class BNPaymentHandler {

    /**
     * A variable representing the base URL of the API.
     */
    private String mBaseUrl;

    private static BNPaymentHandler ourInstance = new BNPaymentHandler();

    /**
     * A {@link BNHttpClient} used for communicating to the API.
     */
    private BNHttpClient mHttpClient;

    /**
     * An api-token to identify the Application to the API.
     */
    private String mApiToken;

    /**
     * A boolean indicating whether setup is done.
     */
    private boolean mIsSetup;

    /**
     * A boolean indicating whether lib is in debug mode
     */
    private boolean mDebug;

    private CreditCardManager creditCardManager = new CreditCardManager();

    /**
     * Empty constructor for {@link BNPaymentHandler}
     */
    private BNPaymentHandler() {}

    /**
     * This method is used for accessing the shared BNPaymentHandler.
     *
     * @return An instance of BNPaymentHandler
     */
    public static BNPaymentHandler getInstance() {
        return ourInstance;
    }

    /**
     * Method for setting up {@link BNPaymentHandler}. Must be called before using the handler.
     * This method is required to set context, appId and countryId.
     *
     * @param builder A builder class for {@link BNPaymentHandler}
     */
    public static void setupBNPayments(BNPaymentBuilder builder) {
        if (!ourInstance.mIsSetup) {
            ourInstance.mBaseUrl = builder.baseUrl;
            ourInstance.mApiToken = builder.apiToken;
            ourInstance.mDebug = builder.debug;
            ourInstance.mIsSetup = true;
            ourInstance.mHttpClient = new BNHttpClient(builder.context, builder.apiToken);
        }
    }

    /**
     * Initiates the hosted payment page for registering a credit card and returns the URL to the
     * page.
     * @param formSettings  HPP form settings
     * @param listener      Callback listener
     */
    public Request initiateHostedPaymentPage(RegistrationFormSettings formSettings, PaymentApiService.IHppResultListener listener) {
        return PaymentService.initiateHppRegistration(formSettings, listener);
    }

    /**
     * Reads all stored credit cards from local storage asynchronously and notifies listener.
     * @param context  App Context object
     * @param listener Callback listener; called when list of cards have been loaded
     */
    public void getRegisteredCreditCards(Context context, CreditCardManager.IOnCreditCardRead listener) {
        creditCardManager.getCreditCards(context, listener);
    }

    /**
     * Deletes a stored credit card with given token from local disk.
     * @param context           App context object
     * @param creditCardToken   Token for credit card to be deleted
     * @param listener          Result listener
     */
    public void deleteCreditCard(Context context, String creditCardToken, CreditCardManager.IOnCreditCardDeleted listener) {
        creditCardManager.deleteCreditCard(context, creditCardToken, listener);
    }

    /**
     * Updates credit card alias for card with given token.
     * @param context   App Context object
     * @param alias     Name/Alias for the card
     * @param token     Credit card token
     * @param listener  Status listener. Called when update is completed.
     */
    public void setCreditCardAlias(Context context, String alias, String token, CreditCardManager.IOnCreditCardSaved listener) {
        creditCardManager.setAlias(context, alias, token, listener);
    }

    /**
     * Makes a transaction as specified in {@link PaymentSettings}.
     * @param paymentIdentifier Identifier of the payment that this transaction refers to.
     * @param paymentSettings   Specifies details of this transaction.
     * @param callBack          Transaction result callback.
     */
    public Request makeTransaction(String paymentIdentifier, PaymentSettings paymentSettings, ITransactionListener callBack) {
        return PaymentService.makeTransaction(paymentIdentifier, paymentSettings, callBack);
    }

    /**
     * Instantiates and initiates an {@link ApiService} with a base URL and a network client.
     * <p>The network client is used to customize all requests sent by the service.</p>
     *
     * @param serviceClass  Class to be initiated. Must extend {@link ApiService}.
     * @param <T>           Generic class to instantiate
     * @return The initiated service class object.
     */
    public <T extends ApiService> T createService(Class<T> serviceClass) {
        try {
            Constructor<T> constructor = serviceClass.getConstructor();
            T instance = constructor.newInstance();
            instance.initiate(mBaseUrl, mHttpClient);
            return instance;
        } catch (Exception e) {
            BNLog.e(getClass().getSimpleName(), "Internal error; failed to generate API service.", e);
        }
        return null;
    }

    /**
     * Returns the api-token registered to the handler instance.
     *
     * @return The app id used by the {@link BNPaymentHandler}.
     */
    public String getApiToken() {
        return mApiToken;
    }

    /**
     * A getter for debug boolean
     *
     * @return A boolean indicating whether the Lib is in debug mode
     */
    public boolean getDebug() {
        return mDebug;
    }

    /**
     * {@link BNPaymentBuilder} is a class used
     * for building and instance of {@link BNPaymentHandler}. There are three required params
     * taken in by the constructor and two optional.
     */
    public static class BNPaymentBuilder {
        private String baseUrl = "https://eu-native.bambora.com/";
        private boolean debug;
        private String apiToken;
        private Context context;

        /**
         * Constructor for {@link BNPaymentBuilder}
         * that takes the required params.
         *
         * @param context The context in which the handler will operate (Should be the application context)
         * @param apiToken The app id for this application
         */
        public BNPaymentBuilder(Context context, String apiToken) {
            this.context = context;
            this.apiToken = apiToken;
        }

        /**
         * A method for setting the debug flag for the builder.
         *
         * @param debug A flag indicating whether the lib is debug mode or not
         * @return The builder with debug variable set
         */
        public BNPaymentBuilder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        /**
         * A method for setting the base url for the builder.
         *
         * @param baseUrl The base Url for the endpoint used for network communication
         * @return The builder with debug variable set
         */
        public BNPaymentBuilder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }
    }
}
