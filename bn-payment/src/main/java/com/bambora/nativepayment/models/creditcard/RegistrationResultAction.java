package com.bambora.nativepayment.models.creditcard;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Registration action meta data
 * @author Lovisa Corp
 */
public class RegistrationResultAction {

    private static final String KEY_CODE = "code";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_TYPE = "type";
    /**
     * Result code for current action
     */
    public String code;

    /**
     * Tells which action was the source of this result
     */
    public String source;

    /**
     * Type of action
     */
    public String type;

    public ActionCode getActionCode() {
        return code != null ? ActionCode.fromString(code) : ActionCode.UNKNOWN;
    }

    public RegistrationResultAction(JSONObject jsonObject) throws JSONException {
        code = jsonObject.optString(KEY_CODE);
        source = jsonObject.optString(KEY_SOURCE);
        type = jsonObject.optString(KEY_TYPE);
    }

    /**
     * Possible result/status codes for credit card registration callbacks.
     */
    public enum ActionCode {
        SUBMISSION_STARTED("100"),
        SUCCESS("200"),
        SUBMISSION_DECLINED("300"),
        SESSION_ERROR("400"),
        SYSTEM_ERROR("1000"),
        UNKNOWN("9999");

        private String value;
        private static Map<String, ActionCode> stringToActionCodeMap = new HashMap<>();

        ActionCode(String value) {
            this.value = value;
        }
        static {
            for (ActionCode actionCode : ActionCode.values()) {
                stringToActionCodeMap.put(actionCode.value, actionCode);
            }
        }

        public static ActionCode fromString(String value) {
            ActionCode actionCode = stringToActionCodeMap.get(value);
            return actionCode != null ? actionCode : UNKNOWN;
        }
    }
}
