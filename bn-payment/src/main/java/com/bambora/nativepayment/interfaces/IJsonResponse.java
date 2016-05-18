package com.bambora.nativepayment.interfaces;

import org.json.JSONException;

/**
 * Interface for parsing a model from a JSON string.
 */
public interface IJsonResponse<T> {
    T fromJson(String jsonString) throws JSONException;
}
