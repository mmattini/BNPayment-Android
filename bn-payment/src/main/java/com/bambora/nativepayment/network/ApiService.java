package com.bambora.nativepayment.network;

/**
 * Abstract class for services handling network requests.
 */
public abstract class ApiService {

    /**
     * Base URL to host that the service is to communicate with.
     */
    private String baseUrl;

    /**
     * An {@link HttpClient} to be used for customising all requests sent from the service.
     */
    private HttpClient client;

    /**
     * Initiates the service by setting base URL and client.
     *
     * @param baseUrl   Host base URL
     * @param client    {@link HttpClient} for customising requests
     */
    public void initiate(String baseUrl, HttpClient client) {
        this.baseUrl = baseUrl;
        this.client = client;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected HttpClient getClient() {
        return client;
    }

    protected String createUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
