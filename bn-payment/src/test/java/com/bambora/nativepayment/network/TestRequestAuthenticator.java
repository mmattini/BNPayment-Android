package com.bambora.nativepayment.network;

import com.bambora.nativepayment.models.Credentials;
import com.bambora.nativepayment.utils.HashUtils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * TODO
 */
@RunWith(Parameterized.class)
public class TestRequestAuthenticator {

    @Parameters
    public static Collection<Object[]> data() {
        String currentDate = new Date().toString();
        return Arrays.asList(new Object[][] {
                { "{\"key\":\"body\"}", "{\"key\":\"body\"}", currentDate, currentDate, "/a/path/", "/a/path/", "query", "query" },
                { "", null, "", null, "", null, "", null },
                { "", "", "", "", "", "", "", "" }
        });
    }

    private static final String AUTH_PREFIX = "MPS1";
    private static final String AUTH_DELIMITERS = " |:";
    private static final String SHARED_SECRET = "SharedSecret";
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private String uuid;
    private String expectedBody;
    private String inputBody;
    private String expectedDate;
    private String inputDate;
    private String expectedPath;
    private String inputPath;
    private String expectedQuery;
    private String inputQuery;

    public TestRequestAuthenticator(
            String expectedBody, String inputBody,
            String expectedDate, String inputDate,
            String expectedPath, String inputPath,
            String expectedQuery, String inputQuery) {
        this.uuid = UUID.randomUUID().toString();
        this.expectedBody = expectedBody;
        this.inputBody = inputBody;
        this.expectedDate = expectedDate;
        this.inputDate = inputDate;
        this.expectedPath = expectedPath;
        this.inputPath = inputPath;
        this.expectedQuery = expectedQuery;
        this.inputQuery = inputQuery;
    }

    @Test
    public void shouldCreateValidAuthenticator() throws Exception {
        Credentials credentials = new Credentials(uuid, SHARED_SECRET);
        RequestAuthenticator requestAuthenticator = new RequestAuthenticator(
                credentials,
                inputBody,
                inputDate,
                inputPath,
                inputQuery
        );
        String authHeader = requestAuthenticator.generateRequestHeader();
        verifyAuthenticatorString(authHeader, expectedDate, expectedBody, expectedPath, expectedQuery);
    }

    private void verifyAuthenticatorString(String authenticatorString,
               String expectedDateString, String expectedBodyString, String expectedPath,
               String expectedQuery) throws Exception {
        String[] split = authenticatorString.split(AUTH_DELIMITERS);
        Assert.assertEquals(3, split.length);

        String prefix = split[0];
        String uuid = split[1];
        String hash = split[2];

        Assert.assertEquals(AUTH_PREFIX, prefix);
        Assert.assertEquals(this.uuid, uuid);

        String expectedHashString =
                this.uuid
                        + ":" + expectedBodyString
                        + ":" + expectedDateString
                        + ":" + expectedPath
                        + ":" + expectedQuery;
        String calculatedHash = HashUtils.generateHmac(HMAC_ALGORITHM, expectedHashString, SHARED_SECRET);
        Assert.assertEquals(calculatedHash, hash);
    }
}
