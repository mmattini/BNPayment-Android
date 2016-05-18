package com.bambora.nativepayment.network;

import android.content.Context;
import android.util.Log;

import com.bambora.nativepayment.interfaces.ICredentialsObserver;
import com.bambora.nativepayment.interfaces.IGetCredentialsCallback;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.Credentials;
import com.bambora.nativepayment.models.UserModel;
import com.bambora.nativepayment.services.CredentialsApiService.CredentialsService;
import com.bambora.nativepayment.storage.FileStorage;
import com.bambora.nativepayment.utils.DateUtils;
import com.bambora.nativepayment.utils.HashUtils;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * {@link BNHttpClient} is used for customizing requests to the Bambora API. The main purpose
 * is using an {@link Credentials} to generate an authentication string to be set as the
 * Authorization header field value. This is required in order to communicate with the back-end.
 */
public class BNHttpClient extends HttpClient {

    private static final int API_VERSION = 1;

    /**
     * The {@link Credentials} used for this client.
     */
    private Credentials mCredentials;

    /**
     * The API token used by this client.
     */
    private String mApiToken;

    private Context mContext;
    private CredentialsLoader credentialsLoader = new CredentialsLoader();

    /**
     * A constructor that takes an {@link Credentials} to be used by the instance when
     * generating an authentication header.
     *
     * @param context
     * @param apiToken
     */
    public BNHttpClient(Context context, final String apiToken) {
        this.mContext = context;
        this.mApiToken = apiToken != null ? apiToken : "";
    }

    /**
     * Adds headers for all requests sent using this client.
     *
     * @param request   A {@link Request} object
     */
    @Override
    public void addDefaultHeaders(Request request) {
        request
            .header("Accept", "application/json")
            .header("Api-Token", mApiToken)
            .header("Content-Type", "application/json")
            .header("Api-Version", String.valueOf(API_VERSION));
    }

    /**
     * Calculates the authentication hash and adds it as a header for all request requiring
     * authentication.
     *
     * @param request   A {@link Request} object.
     * @param listener  Result listener
     */
    @Override
    public void addAuthenticator(final Request request, final IAuthenticatorListener listener) {
        if (mCredentials != null) {
            addAuthHeader(request);
            listener.onAuthenticatorAdded();
        } else {
            credentialsLoader.getCredentials(new ICredentialsObserver() {
                @Override
                public void onCredentialsLoaded() {
                    addAuthHeader(request);
                    listener.onAuthenticatorAdded();
                }

                @Override
                public void onCredentialsError(RequestError error) {
                    BNLog.e(getClass().getSimpleName(), "Failed to create authentication header for request.");
                    listener.onAuthenticatorError(error);
                }
            });
        }
    }

    /**
     * A method for adding an authentication header to the request Authorization header.
     *
     * @param request The request which to set the auth header on
     * @return A request with Authorization header set.
     */
    private void addAuthHeader(Request request) {
        String bodyString = request.getRawBody();
        String dateHeader = DateUtils.getDateHeader(new Date());
        String path = request.getUrl().getPath();
        String query = request.getUrl().getQuery() == null ? "" : request.getUrl().getQuery();
        String authHeader = generateAuthHeader(bodyString, dateHeader, path, query);
        request
            .header("Authorization", authHeader)
            .header("Date", dateHeader);
    }

    /**
     * A method for generating an Auth header.
     * The auth header consists protocol followed by app id, uuid and a hmac separated by ':'.
     * The hmac itself i generated
     *
     * @param httpBody A UTF-8 String representation of the HTTP-body.
     * @param dateHeader The http date header field.
     * @param path The http path field.
     * @param query The http query field.
     * @return The generated Auth header
     */
    private String generateAuthHeader(String httpBody, String dateHeader, String path, String query) {
        String authHeader = "";
        if (mCredentials != null) {

            String stringToHash = mCredentials.getUuid()
                    + ":" + httpBody
                    + ":" + dateHeader
                    + ":" + path
                    + ":" + query;

            String hmac = null;
            try {
                hmac = HashUtils.generateHmac("HmacSHA256", stringToHash, mCredentials.getSharedSecret());
            } catch (GeneralSecurityException e) {
                Log.w("", "Generating hmac failed: " + e);
            }

            authHeader = "MPS1 " + mCredentials.getUuid() + ":" + hmac;
        }

        return authHeader;
    }

    /**
     * Saves an {@link Credentials} to disk
     *
     * @param credentials An {@link Credentials} used for authentication call to the API
     */
    private void saveCredentials(Credentials credentials) {
        FileStorage fileStorage = new FileStorage();
        fileStorage.saveCredentials(mContext, credentials);
    }

    /**
     * A class for handling loading of credentials for API requests. It first tries to load them
     * from disk and if no credentials are found it will download a new set from the server. If
     * called multiple times during loading a list of observers will be saved and then notified
     * when loading has completed (to avoid multiple downloads of credentials).
     */
    private class CredentialsLoader {

        /**
         * List of result observers.
         */
        private List<ICredentialsObserver> observers = new ArrayList<>();

        /**
         * Loads credentials if another load isn't ongoing. Otherwise the observer is only added to
         * a list for notification.
         *
         * @param observer  Result listener
         */
        private void getCredentials(ICredentialsObserver observer) {
            observers.add(observer);
            if (isFirst(observer)) {
                loadCredentials();
            }
        }

        /**
         * Checks if given observer is first in the list of observers.
         *
         * @param observer  An {@link ICredentialsObserver}
         * @return True if first in list
         */
        private boolean isFirst(ICredentialsObserver observer) {
            return observers.indexOf(observer) == 0;
        }

        /**
         * Tries to load {@link Credentials} from disk and if not found it downloads a new set.
         */
        private void loadCredentials() {
            FileStorage fileStorage = new FileStorage();
            fileStorage.getCredentials(mContext, new FileStorage.IOnObjectRead() {
                @Override
                public void notifyOnObjectRead(Serializable object) {
                    boolean validCredentials = object != null && object.getClass() == Credentials.class;
                    if (validCredentials) {
                        mCredentials = (Credentials) object;
                        notifyObservers(null);
                    } else {
                        downloadCredentials();
                    }
                }
            });
        }

        /**
         * Downloads {@link Credentials} from server and saves them to disk if successful.
         */
        private void downloadCredentials() {
            UserModel userModel = new UserModel();
            CredentialsService.getCredentials(userModel, new IGetCredentialsCallback() {
                @Override
                public void onCredentialsReceived(Credentials credentials) {
                    mCredentials = credentials;
                    saveCredentials(mCredentials);
                    notifyObservers(null);
                }

                @Override
                public void onError(RequestError error) {
                    notifyObservers(error);
                }
            });
        }

        /**
         * Notifies all observers with the result of loading {@link Credentials}.
         *
         * @param requestError  Error object. Null if successful.
         */
        private void notifyObservers(RequestError requestError) {
            Iterator<ICredentialsObserver> iterator = observers.iterator();
            while (iterator.hasNext()) {
                ICredentialsObserver observer = iterator.next();
                if (requestError == null) {
                    observer.onCredentialsLoaded();
                } else {
                    observer.onCredentialsError(requestError);
                }
                iterator.remove();
            }
        }
    }
}
