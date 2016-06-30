package com.bambora.nativepayment.models;

import com.bambora.nativepayment.interfaces.IJsonRequest;
import com.bambora.nativepayment.logging.BNLog;

import org.json.JSONException;
import org.json.JSONObject;

import static org.json.JSONObject.NULL;

/**
 * This is a model representing an User in the SDK.
 * This model is made for adding metadata to an app registration.
 *
 * Created by oskarhenriksson on 29/02/16.
 */
public class UserModel implements IJsonRequest {

    private static final String KEY_EXTERNAL_REFERENCE = "external_reference";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE_NUMBER = "phone_number";
    private static final String KEY_NAME = "name";
    private static final String KEY_USER_NAME = "username";

    /**
     * The merchant reference for the user
     */
    private String externalReference;

    /**
     * The email of the user
     */
    private String email;

    /**
     * The phone number of the user
     */
    private String phoneNumber;

    /**
     * The name of the user
     */
    private String name;

    /**
     * The username of the user
     */
    private String username;

    /**
     * Getter for externalReference.
     *
     * @return Value of externalReference variable.
     */
    public String getExternalReference() {
        return externalReference;
    }

    /**
     * Getter for email.
     *
     * @return Value of email variable.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for phoneNumber.
     *
     * @return Value of phoneNumber variable.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Getter for name.
     *
     * @return Value of name variable.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for username.
     *
     * @return Value of username variable.
     */
    public String getUsername() {
        return username;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getSerialized() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(KEY_EXTERNAL_REFERENCE, externalReference != null ? externalReference : NULL);
            jsonObject.put(KEY_EMAIL, email != null ? email : NULL);
            jsonObject.put(KEY_PHONE_NUMBER, phoneNumber != null ? phoneNumber : NULL);
            jsonObject.put(KEY_NAME, name != null ? name : NULL);
            jsonObject.put(KEY_USER_NAME, username != null ? username : NULL);
        } catch (JSONException e) {
            BNLog.jsonParseError(getClass().getSimpleName(), e);
        }
        return jsonObject.toString();
    }
}