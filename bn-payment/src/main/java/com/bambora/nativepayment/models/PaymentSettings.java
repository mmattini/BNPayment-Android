package com.bambora.nativepayment.models;


import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link PaymentSettings} is a model representing the information needed to initiate a transaction.
 *
 * Created by oskarhenriksson on 15/10/15.
 */
public class PaymentSettings implements IJsonRequest {

    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_TOKEN = "token";

    public Integer amount;
    public String currency;
    public String comment;
    public String creditCardToken;

    @Override
    public String getSerialized() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_AMOUNT, amount);
            jsonObject.put(KEY_CURRENCY, currency);
            jsonObject.put(KEY_COMMENT, comment);
            jsonObject.put(KEY_TOKEN, creditCardToken);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return jsonObject.toString();
    }
}
