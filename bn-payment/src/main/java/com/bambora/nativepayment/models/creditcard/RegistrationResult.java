package com.bambora.nativepayment.models.creditcard;

import com.bambora.nativepayment.models.creditcard.RegistrationResultAction.ActionCode;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Lovisa Corp
 */
public class RegistrationResult {

    private static final String KEY_META = "meta";
    private static final String KEY_CARD_NUMBER = "truncatedcardnumber";
    private static final String KEY_EXPIRY_MONTH = "expmonth";
    private static final String KEY_EXPIRY_YEAR = "expyear";
    private static final String KEY_PAYMENT_TYPE = "paymenttype";
    private static final String KEY_TRANSACTION_ID = "transactionid";
    private static final String KEY_SUBSCRIPTION_ID = "subscriptionid";
    private static final String KEY_ORIGIN_IP = "originip";

    /**
     * {@link RegistrationResultMeta} object
     */
    public final RegistrationResultMeta meta;

    /**
     * Truncated credit card number
     */
    public final String truncatedCardNumber;

    /**
     * Card expiry month
     */
    public final Integer expiryMonth;

    /**
     * Card axpry year
     */
    public final Integer expiryYear;

    /**
     * Payment type, such as Visa or MasterCard
     */
    public final String paymentType;

    /**
     * ID of transaction created at the time of card registration
     */
    public final String transactionId;

    /**
     * Subsription identifier
     */
    public final String subscriptionId;

    /**
     * Origin IP address from where credit card registration was made
     */
    public final String originIp;

    public RegistrationResult(JSONObject jsonObject) throws JSONException {
        meta = new RegistrationResultMeta(jsonObject.getJSONObject(KEY_META));
        truncatedCardNumber = jsonObject.optString(KEY_CARD_NUMBER);
        expiryMonth = jsonObject.optInt(KEY_EXPIRY_MONTH);
        expiryYear = jsonObject.optInt(KEY_EXPIRY_YEAR);
        paymentType = jsonObject.optString(KEY_PAYMENT_TYPE);
        transactionId = jsonObject.optString(KEY_TRANSACTION_ID);
        subscriptionId = jsonObject.optString(KEY_SUBSCRIPTION_ID);
        originIp = jsonObject.optString(KEY_ORIGIN_IP);
    }

    public static RegistrationResult fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return new RegistrationResult(jsonObject);
    }

    public ActionCode getActionCode() {
        if (meta != null && meta.action != null) {
            return meta.action.getActionCode();
        } else {
            return ActionCode.UNKNOWN;
        }
    }
}
