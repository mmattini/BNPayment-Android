package com.bambora.nativepayment.models;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * This is a model used for authenticating against the backend.
 */
public class Credentials implements IJsonResponse<Credentials>, Serializable {

    private static final String KEY_ID = "id";
    private static final String KEY_SECRET = "secret";

    /**
     * The uuid of the user/app installation
     */
    private String uuid;

    /**
     * The shared secret between back-end and app
     */
    private String sharedSecret;

    public String getUuid() {
        return uuid;
    }

    public String getSharedSecret() {
        return sharedSecret;
    }

    @Override
    public Credentials fromJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            uuid = jsonObject.getString(KEY_ID);
            sharedSecret = jsonObject.getString(KEY_SECRET);
        } catch (JSONException exception) {
            BNLog.jsonParseError(getClass().getSimpleName(), exception);
        }
        return this;
    }
}
