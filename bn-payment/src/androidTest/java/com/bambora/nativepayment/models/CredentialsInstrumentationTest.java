package com.bambora.nativepayment.models;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * TODO
 */
@RunWith(AndroidJUnit4.class)
public class CredentialsInstrumentationTest extends InstrumentationTestCase {

    private Credentials credentials;

    public void setUp() {
        this.credentials = new Credentials("defaultUUID", "defaultSecret");
    }

    public void testFromJsonWithEmptyJson() {
        // Given
        String emptyJsonString = "{}";

        // When
        this.credentials.fromJson(emptyJsonString);

        // Then
        Assert.assertNull(this.credentials.getUuid());
        Assert.assertNull(this.credentials.getSharedSecret());
    }

    public void testFromJsonWithUuid() {
        // Given
        String jsonString = "{\"id\": \"uuid\" }";

        // When
        this.credentials.fromJson(jsonString);

        // Then
        Assert.assertEquals("uuid", this.credentials.getUuid());
        Assert.assertNull(this.credentials.getSharedSecret());
    }

    public void testFromJsonWithSecret() {
        // Given
        String jsonString = "{\"secret\": \"shared secret\" }";

        // When
        this.credentials.fromJson(jsonString);

        // Then
        Assert.assertNull(this.credentials.getUuid());
        Assert.assertEquals("shared secret", this.credentials.getSharedSecret());
    }

    public void testFromJsonWithUuidAndSecret() {
        // Given
        String jsonString = "{\"secret\": \"shared secret\", \"id\": \"uuid\" }";

        // When
        this.credentials.fromJson(jsonString);

        // Then
        Assert.assertEquals("uuid", this.credentials.getUuid());
        Assert.assertEquals("shared secret", this.credentials.getSharedSecret());
    }

    public void testFromJsonWithInvalidJson() {
        // Given
        String invalidJsonString = "{";

        // When
        this.credentials.fromJson(invalidJsonString);

        // Then
        Assert.assertEquals("defaultUUID", this.credentials.getUuid());
        Assert.assertEquals("defaultSecret", this.credentials.getSharedSecret());
    }
}