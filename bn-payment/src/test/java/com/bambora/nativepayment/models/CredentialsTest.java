package com.bambora.nativepayment.models;

import org.junit.Assert;
import org.junit.Test;

/**
 * TODO
 */
public class CredentialsTest {

    @Test
    public void getUuidDefaultValue() {
        // When
        Credentials credentials = new Credentials();

        // Then
        Assert.assertNull(credentials.getUuid());
    }

    @Test
    public void getUuidSpecifiedValue() {
        // When
        Credentials credentials = new Credentials("uuid", null);

        // Then
        Assert.assertEquals("uuid", credentials.getUuid());
    }

    @Test
    public void getSharedSecretDefaultValue() {
        // When
        Credentials credentials = new Credentials();

        // Then
        Assert.assertNull(credentials.getSharedSecret());
    }

    @Test
    public void getSharedSecretSpecifiedValue() {
        // When
        Credentials credentials = new Credentials(null, "shared secret");

        // Then
        Assert.assertEquals("shared secret", credentials.getSharedSecret());
    }
}