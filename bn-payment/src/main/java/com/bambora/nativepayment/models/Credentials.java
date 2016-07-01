/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

    public Credentials() {}

    public Credentials(String uuid, String sharedSecret) {
        this.uuid = uuid;
        this.sharedSecret = sharedSecret;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getSharedSecret() {
        return this.sharedSecret;
    }

    @Override
    public Credentials fromJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            this.uuid = jsonObject.optString(KEY_ID, null);
            this.sharedSecret = jsonObject.optString(KEY_SECRET, null);
        } catch (JSONException exception) {
            BNLog.jsonParseError(getClass().getSimpleName(), exception);
        }
        return this;
    }
}
