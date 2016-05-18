package com.bambora.nativepayment.models.creditcard;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model for response received when initiating a hosted payment page
 */
public class RegistrationResponse implements IJsonResponse<RegistrationResponse> {

    private static final String KEY_SESSION_URL = "session_url";

    public String sessionUrl;

    @Override
    public RegistrationResponse fromJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            sessionUrl = jsonObject.optString(KEY_SESSION_URL);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return this;
    }
}
