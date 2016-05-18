package com.bambora.nativepayment.models.creditcard;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Hold meta data of the card registration session
 * @author Lovisa Corp
 */
public class RegistrationResultMeta {

    private static final String KEY_ACTION = "action";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_RESULT = "result";

    /**
     * {@link RegistrationResultAction} object
     */
    public RegistrationResultAction action;

    /**
     * {@link RegistrationResultMessage} object
     */
    public RegistrationResultMessage message;

    /**
     * True if result is included
     */
    public final Boolean result;

    public RegistrationResultMeta(JSONObject jsonObject) throws JSONException {
        this.action = new RegistrationResultAction(jsonObject.optJSONObject(KEY_ACTION));
        this.message = new RegistrationResultMessage(jsonObject.optJSONObject(KEY_MESSAGE));
        this.result = jsonObject.optBoolean(KEY_RESULT);
    }
}
