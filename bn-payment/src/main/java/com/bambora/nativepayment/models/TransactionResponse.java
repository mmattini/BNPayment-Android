package com.bambora.nativepayment.models;

import com.bambora.nativepayment.interfaces.IJsonResponse;
import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.creditcard.TransactionErrorResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link TransactionResponse} is a model representing a transaction response in the system.
 */
public class TransactionResponse implements IJsonResponse<TransactionResponse> {

    private static final String KEY_PAYMENT_ID = "payment";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CODE = "code";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_INFO = "info";

    public String paymentId;

    public Integer amount;

    public String currency;

    /**
     * Error response parameters
     */

    public Integer status;

    public Integer code;

    public String errorMessage;

    public String errorInfo;

    @Override
    public TransactionResponse fromJson(String jsonString) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);
            paymentId = jsonObject.optString(KEY_PAYMENT_ID);
            amount = jsonObject.optInt(KEY_AMOUNT);
            currency = jsonObject.optString(KEY_CURRENCY);
            status = jsonObject.optInt(KEY_STATUS);
            code = jsonObject.optInt(KEY_CODE);
            errorMessage = jsonObject.optString(KEY_MESSAGE);
            errorInfo = jsonObject.optString(KEY_INFO);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return this;
    }

    public TransactionErrorResponse getError() {
        return new TransactionErrorResponse(
                this.status,
                this.code,
                this.errorMessage,
                this.errorInfo
        );
    }
}
