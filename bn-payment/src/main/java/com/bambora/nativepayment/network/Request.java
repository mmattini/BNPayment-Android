package com.bambora.nativepayment.network;

import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.logging.BNLog;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Model of a network request that handles data needed to make an HTTP REST call.
 */
public class Request<T extends IJsonResponse<T>> {

    private static final String LOG_TAG = "BNNetworkRequest";

    /**
     * The service that this request is sent from.
     */
    private ApiService service;

    /**
     * The URL path for the endpoint (excluding the base URL) that the request should be sent to.
     */
    private String endpoint;

    /**
     * If the endpoint contains REST parameters they are stored in this Map as Map<key, value>.
     */
    private Map<String, String> endpointParameters;

    /**
     * REST request method. Default is 'GET'.
     */
    private RequestMethod method = RequestMethod.GET;

    /**
     * HTTP header class for all header fields
     */
    private HttpHeader header = new HttpHeader();

    /**
     * Class of the expected response body.
     */
    private Class<T> responseClass;

    /**
     * The request body.
     */
    private IJsonRequest body;

    private boolean requireAuthenticator = true;

    public Request(ApiService service, Class<T> responseClass) {
        this.service = service;
        this.responseClass = responseClass;
    }

    /**
     * Sets the endpoint path.
     *
     * @param endpoint  Request endpoint path
     * @return This {@link Request}
     */
    public Request<T> endpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Adds an endpoint REST parameter with given key and value.
     *
     * @param key   Parameter name
     * @param value Value of the parameter
     * @return This {@link Request}
     */
    public Request<T> endpointParameter(String key, String value) {
        if (endpointParameters == null) {
            endpointParameters = new HashMap<>();
        }
        endpointParameters.put(key, value);
        return this;
    }

    /**
     * Sets the request method.
     *
     * @param method    A REST request method
     * @return This {@link Request}
     */
    public Request<T> method(RequestMethod method) {
        this.method = method;
        return this;
    }

    /**
     * Adds an HTTP header field with given key and value.
     *
     * @param key   Header field name
     * @param value Header field value
     * @return This {@link Request}
     */
    public Request<T> header(String key, String value) {
        this.header.addField(key, value);
        return this;
    }

    /**
     * Sets the body of this request.
     *
     * @param body  Body object
     * @return This {@link Request}
     */
    public Request<T> body(IJsonRequest body) {
        this.body = body;
        return this;
    }

    public Request<T> requireAuthenticator(boolean requireAuthenticator) {
        this.requireAuthenticator = requireAuthenticator;
        return this;
    }

    /**
     * Returns the request URL formatted with given endpoint parameters.
     *
     * @return A {@link URL}
     */
    public URL getUrl() {
        try {
            String formattedEndpoint = setEndpointParameters(endpoint);
            return new URL(service.createUrl(formattedEndpoint));
        } catch (MalformedURLException e) {
            BNLog.e(LOG_TAG, "Failed to create request. Invalid URL. ", e);
        }
        return null;
    }

    /**
     * Returns the request method as a {@link String}.
     *
     * @return Request method
     */
    public String getMethod() {
        return method.value();
    }

    /**
     * Returns the {@link HttpHeader}.
     *
     * @return The {@link HttpHeader}
     */
    public HttpHeader getHeader() {
        return header;
    }

    /**
     * Formats and returns the body as a JSON {@link String}.
     *
     * @return A JSON formatted {@link String} of the request body.
     */
    public String getRawBody() {
        return body.getSerialized();
    }

    public boolean requiresAuthenticator() {
        return requireAuthenticator;
    }

    /**
     * Returns the response body class.
     *
     * @return {@link Class} of the response body
     */
    public Class<T> getResponseClass() {
        return responseClass;
    }

    /**
     * Executes this request using the {@link HttpClient} of the given {@link ApiService}.
     *
     * @param callback  Result listener
     */
    public void execute(Callback<T> callback) {
        new RequestExecutor<T>(service.getClient()).executeRequest(this, responseClass, callback);
    }

    /**
     * Creates a log friendly String of this request.
     *
     * @return A {@link String} representation of the class.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("URL: ").append(getUrl()).append("\n")
                .append("Request method: ").append(method).append("\n")
                .append("Request header:\n")
                .append(header.toString());
        String rawBody = getRawBody();
        if (rawBody != null) {
            builder
                    .append("Request body:\n")
                    .append("\t\t").append(rawBody).append("\n");
        }
        return builder.toString();
    }

    private String setEndpointParameters(String endpoint) {
        String placeholderFormat = "{value}";
        if (endpointParameters != null) {
            for (String key : endpointParameters.keySet()) {
                String placeholder = placeholderFormat.replace("value", key);
                endpoint = endpoint.replace(placeholder, endpointParameters.get(key));
            }
        }
        return endpoint;
    }
}
