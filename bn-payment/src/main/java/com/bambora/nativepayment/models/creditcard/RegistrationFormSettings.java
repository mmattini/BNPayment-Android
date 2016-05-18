package com.bambora.nativepayment.models.creditcard;

import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Used for customizing the credit card registration form.
 * <p>This model is to be attached in the credit card registration request so fields and input
 * group keys shouldn't be changed unless the API changes.</p>
 *
 * @author Lovisa Corp
 */
public class RegistrationFormSettings implements IJsonRequest {

    private static final String INPUT_GROUP_CARD_NUMBER =  "cardnumber";
    private static final String INPUT_GROUP_EXPIRY_DATE =  "cardexpiry";
    private static final String INPUT_GROUP_CVV =          "cardverification";

    private static final String KEY_PLATFORM = "platform";
    private static final String KEY_CSS_URL = "cssurl";
    private static final String KEY_INPUT_GROUPS = "inputgroups";
    private static final String KEY_SUBMIT_BUTTON = "submitbutton";

    private InputGroup cardNumberInputGroup = new InputGroup(INPUT_GROUP_CARD_NUMBER);
    private InputGroup cardExpiryDateInputGroup = new InputGroup(INPUT_GROUP_EXPIRY_DATE);
    private InputGroup cardCvvInputGroup = new InputGroup(INPUT_GROUP_CVV);

    /**
     * Describes which platform the form is intended for
     */
    private String platform = "android";

    /**
     * URL to CSS file for styling the credit card registration form
     * <p>Use {@link #setCssUrl(String)} to set.</p>
     */
    private String cssUrl;

    /**
     * List of {@link InputGroup}
     * <p>Sets placeholder texts for input values in the registration form.</p>
     */
    private List<InputGroup> inputGroups;

    /**
     * Text for the submit button
     */
    private String submitButtonText;

    public RegistrationFormSettings() {
        inputGroups = new ArrayList<>();
        inputGroups.add(cardNumberInputGroup);
        inputGroups.add(cardExpiryDateInputGroup);
        inputGroups.add(cardCvvInputGroup);
    }

    /**
     * Sets URL to CSS file for formatting the registration form
     *
     * @param cssUrl    URL to CSS file
     */
    public void setCssUrl(String cssUrl) {
        this.cssUrl = cssUrl;
    }

    /**
     * Sets hint for card number input field
     * @param text      Hint text
     */
    public void setCardNumberInputHint(String text) {
        cardNumberInputGroup.placeholder = text;
    }

    /**
     * Sets hint for card expiry input field
     * @param text      Hint text
     */
    public void setCardExpiryInputHint(String text) {
        cardExpiryDateInputGroup.placeholder = text;
    }

    /**
     * Sets hint for CVV input field
     * @param text      Hint text
     */
    public void setCvvInputHint(String text) {
        cardCvvInputGroup.placeholder = text;
    }

    /**
     * Adds an {@link InputGroup} for setting placeholder text on specified input section
     *
     * @param name          Input section class name (CSS class)
     * @param placeholder   Placeholder text visible in specified text field
     */
    public void addInputGroup(String name, String placeholder) {
        if (name != null && !name.isEmpty()) {
            this.inputGroups.add(new InputGroup(name, placeholder));
        }
    }

    /**
     * Sets the text that's visible on the submit button
     * @param text  Button text
     */
    public void setSubmitButtonText(String text) {
        this.submitButtonText = text;
    }

    @Override
    public String getSerialized() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_PLATFORM, platform);
            jsonObject.put(KEY_CSS_URL, cssUrl);
            JSONArray inputGroupArray = new JSONArray();
            for (InputGroup inputGroup : inputGroups) {
                JSONObject inputGroupJsonObject = new JSONObject(inputGroup.getSerialized());
                inputGroupArray.put(inputGroupJsonObject);
            }
            jsonObject.put(KEY_INPUT_GROUPS, inputGroupArray);
            jsonObject.put(KEY_SUBMIT_BUTTON, submitButtonText);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return jsonObject.toString().replace("\\", "");
    }

    /**
     * Model for input group data in the credit card registration form
     */
    public class InputGroup implements IJsonRequest {

        private static final String KEY_NAME = "name";
        private static final String KEY_PLACEHOLDER = "placeholder";

        /**
         * Name of the form section class
         */
        private String name;

        /**
         * Input field placeholder text
         */
        private String placeholder;

        public InputGroup(String name) {
            this(name, "");
        }

        public InputGroup(String name, String placeholder) {
            this.name = name;
            this.placeholder = placeholder;
        }

        @Override
        public String getSerialized() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(KEY_NAME, name);
                jsonObject.put(KEY_PLACEHOLDER, placeholder);
            } catch (JSONException e) {
                BNLog.jsonParseError(getClass().getSimpleName(), e);
            }
            return jsonObject.toString();
        }
    }
}
