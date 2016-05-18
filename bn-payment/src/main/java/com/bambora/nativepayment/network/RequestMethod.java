package com.bambora.nativepayment.network;

/**
 * REST request methods
 */
public enum RequestMethod {
    GET("GET"),
    POST("POST");

    private String value;

    RequestMethod(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
