package com.bambora.nativepayment.network;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Model for a network request error response body.
 */
public class ErrorBody {

    private static final String KEY_STATUS = "status";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DETAIL = "detail";

    public Integer status;

    public String title;

    public String type;

    public String detail;

    public ErrorBody(JSONObject jsonObject) {
        status = jsonObject.optInt(KEY_STATUS);
        title = jsonObject.optString(KEY_TITLE);
        type = jsonObject.optString(KEY_TYPE);
        detail = jsonObject.optString(KEY_DETAIL);
    }

    public static ErrorBody fromJson(String jsonString) throws JSONException {
        JSONObject jsonObject;
        jsonObject = new JSONObject(jsonString);
        return new ErrorBody(jsonObject);
    }
}
