package com.bambora.nativepayment.models.creditcard;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Message for end user
 * @author Lovisa Corp
 */
public class RegistrationResultMessage {

    private static final String KEY_END_USER = "enduser";
    private static final String KEY_MERCHANT = "merchant";

    public final String endUser;

    /**
     * Message for merchant
     */
    public final String merchant;

    public RegistrationResultMessage(JSONObject jsonObject) throws JSONException {
        this.endUser = jsonObject.optString(KEY_END_USER);
        this.merchant = jsonObject.optString(KEY_MERCHANT);
    }
}
