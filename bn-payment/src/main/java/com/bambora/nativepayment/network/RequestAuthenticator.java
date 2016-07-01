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

package com.bambora.nativepayment.network;

import com.bambora.nativepayment.logging.BNLog;
import com.bambora.nativepayment.models.Credentials;
import com.bambora.nativepayment.utils.HashUtils;

import java.security.GeneralSecurityException;

/**
 * Class for generating an authenticator header for BN requests.
 */
public class RequestAuthenticator {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String AUTH_STRING_PREFIX = "MPS1 ";

    private Credentials credentials;
    private String httpBody;
    private String dateHeader;
    private String path;
    private String query;

    public RequestAuthenticator(Credentials credentials, String httpBody, String dateHeader,
                                String path, String query) {
        this.credentials = credentials;
        this.httpBody = httpBody;
        this.dateHeader = dateHeader;
        this.path = path;
        this.query = query;
    }

    /**
     * Generates a string to be attached as request Authorization header value.
     * The auth header consists of protocol followed by app id, uuid and a hmac separated by ':'.
     * The hmac itself is generated.
     *
     * @return String to authorize app in Bambora Native API requests.
     */
    public String generateRequestHeader() {
        String authHeader = "";
        if (credentials != null) {
            String stringToHash = credentials.getUuid()
                    + ":" + (httpBody != null ? httpBody : "")
                    + ":" + (dateHeader != null ? dateHeader : "")
                    + ":" + (path != null ? path : "")
                    + ":" + (query != null ? query : "");

            String hmac = null;
            try {
                hmac = HashUtils.generateHmac(HMAC_ALGORITHM, stringToHash, credentials.getSharedSecret());
            } catch (GeneralSecurityException e) {
                BNLog.w(getClass().getSimpleName(), "Generating hmac failed: ", e);
            }
            authHeader = AUTH_STRING_PREFIX + credentials.getUuid() + ":" + hmac;
        }
        return authHeader;
    }
}
