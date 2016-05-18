package com.bambora.nativepayment.network;

/**
 * Abstract class to be used for customising and executing a network {@link Request}.
 */
public abstract class HttpClient {

    /**
     * Takes a network {@link Request} and adds default HTTP header fields.
     * <p>Called before executing a the request and is used to customise the header of all requests
     * made through this client.</p>
     *
     * @param request   A {@link Request} object
     * @return The {@link Request} object with client specific customisations.
     */
    public abstract void addDefaultHeaders(Request request);

    /**
     * Abstract method for adding authentication to a request.
     *
     * @param request   The {@link Request} to be executed
     * @param listener  Result listener
     */
    public abstract void addAuthenticator(Request request, IAuthenticatorListener listener);

    /**
     * Method for pre-processing all request sent through this client before they are sent.
     *
     * @param request   The {@link Request} to be executed
     * @param listener  Result listener
     */
    public void processRequest(Request request, final IProcessRequestListener listener) {
        addDefaultHeaders(request);
        if (request.requiresAuthenticator()) {
            addAuthenticator(request, new IAuthenticatorListener() {
                @Override
                public void onAuthenticatorAdded() {
                    listener.onRequestProcessed();
                }

                @Override
                public void onAuthenticatorError(RequestError error) {
                    listener.onRequestError(error);
                }
            });
        } else {
             listener.onRequestProcessed();
        }
    }

    public interface IAuthenticatorListener {
        void onAuthenticatorAdded();
        void onAuthenticatorError(RequestError error);
    }

    public interface IProcessRequestListener {
        void onRequestProcessed();
        void onRequestError(RequestError error);
    }
}
