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

package com.bambora.nativepayment.security;

import com.bambora.nativepayment.utils.CertificateUtils;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Local tests for the {@link Crypto} class.
 */
public class CryptoLocalTest {

    private static final String STRING_TO_ENCRYPT = "String to encrypt";
    private static final String RSA_ENCRYPTED_DATA =
                    "W/W6++UhsHpwo8I5Vp5xU/LP1vN+Zba/4yplgw/8mbspn3Zdk+uUpfYCi9AQZd9SrcXHImeUC5Nx3HZ/cL" +
                    "r5061miDaxr272utgn9IB6M9y4l1a4YsLv0NV9Umc82M2FZScAlFCwp5AeMNmP/4VmDJMRsLua7zqMnezM" +
                    "bJN9UBR+p3G9We+Ro+lhhBOF6OciGwWYbdQUxw3xnloh3K+gvsxZoR+n2n/R3nLyrxRJETkCZq3KixUfRx" +
                    "dqX1dyBJOisv3jxu7ftoCFT3gD1TUVWxQ5x2JtSdYVxXUra7LP6C9QbfmiFfpzwDpScROst9owSRNnmh+X" +
                    "V7A17nPMbpmiVtDdU+3SlIO9+sE5WLmzwgnaddL95kqBx/MPtiGy2/41Ylb95pXMqkHlz9pGESdT/KTCIO" +
                    "Xx5aahwdRc/hAKJzkpSgycTNsh4g1TL7RVENr+tQ7UAraToFvFQ4W3q+cmHnCGrlkJGVX0UTu36bz0/4+r" +
                    "P5yH3R2X1+oVB8COHfD4//A2GAtRKLVOgucoP0tx8SvZu8bIfcMQk/QfQ1DYWseAuxyw+B+0gRiqeoaaQy" +
                    "H3TCQSJ/QZFStPqengyNyK0IAlgSEa/aUPcYri+hDnvuE5roPn4NYnEzhARw553giWxZ02DuLFYIuRgRLz" +
                    "+bHig7VAc8SKicFD0jMAsU6D0EQ=";

    private static final String AES_ENCRYPTED_DATA = "lBTaVm9kUS4QKnwiklUNWP2PEdioELFF6FbMsJwT7CA=";
    private static final byte[] AES_TEST_KEY = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

    private Crypto crypto;
    private PrivateKey rsaPrivateKey;
    private PublicKey rsaPublicKey;
    private SecretKey aesKey;

    public CryptoLocalTest() throws Exception {
        crypto = new Crypto();
        aesKey = new SecretKeySpec(AES_TEST_KEY, Crypto.AES_ALGORITHM);
        KeyPair rsaKeyPair = LocalTestData.getKeyPairFromRSAKeystore(getClass().getClassLoader());
        rsaPrivateKey = rsaKeyPair.getPrivate();
        rsaPublicKey = CertificateUtils.parseCertificate(LocalTestData.getCertificateFromString()).getPublicKey();
    }

    @Test
    public void RSAEncryptionShouldSucceed() throws Exception {
        // When
        byte[] encrypted = crypto.RSAEncrypt(toBytes(STRING_TO_ENCRYPT), rsaPublicKey);
        byte[] decrypted = crypto.RSADecrypt(encrypted, rsaPrivateKey);
        String decryptedString = new String(decrypted, Crypto.UTF8_CHARSET);

        // Then
        Assert.assertEquals(STRING_TO_ENCRYPT, decryptedString);
    }

    @Test
    public void RSAEncryptionShouldReturnNull() throws Exception {
        // When
        byte[] encrypted = crypto.RSAEncrypt(null, rsaPublicKey);

        // Then
        Assert.assertNull(encrypted);
    }

    @Test
    public void RSADecryptionShouldReturnEmptyString() throws Exception {
        // When
        byte[] encrypted = crypto.RSAEncrypt(toBytes(""), rsaPublicKey);
        byte[] decrypted = crypto.RSADecrypt(encrypted, rsaPrivateKey);
        String decryptedString = new String(decrypted, Crypto.UTF8_CHARSET);

        // Then
        Assert.assertTrue(decryptedString.isEmpty());
    }

    @Test
    public void RSADecryptionShouldSucceed() throws Exception {
        // When
        byte[] decrypted = crypto.RSADecrypt(Base64.decodeBase64(RSA_ENCRYPTED_DATA), rsaPrivateKey);
        String decryptedString = new String(decrypted, Crypto.UTF8_CHARSET);

        // Then
        Assert.assertEquals(STRING_TO_ENCRYPT, decryptedString);
    }

    @Test(expected = GeneralSecurityException.class)
    public void RSADecryptionShouldFailWhenUsingAnInvalidKey() throws Exception {
        // Given
        String stringToEncrypt = "Secret string";

        // When
        byte[] encrypted = crypto.RSAEncrypt(toBytes(stringToEncrypt), rsaPublicKey);
        PrivateKey invalidKey = crypto.generateRSAKeyPair().getPrivate();
        crypto.RSADecrypt(encrypted, invalidKey);
    }

    @Test
    public void RSADecryptionShouldReturnNull() throws Exception {
        // When
        byte[] result = crypto.RSADecrypt(null, rsaPrivateKey);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void AESEncryptionShouldSucceed() throws Exception {
        // When
        byte[] encrypted = crypto.AESEncrypt(STRING_TO_ENCRYPT, aesKey);

        // Then
        Assert.assertEquals(AES_ENCRYPTED_DATA, Base64.encodeBase64String(encrypted));

        // When
        byte[] decrypted = crypto.AESDecrypt(encrypted, aesKey);
        String decryptedString = new String(decrypted, Crypto.UTF8_CHARSET);

        // Then
        Assert.assertEquals(STRING_TO_ENCRYPT, decryptedString);
    }

    @Test
    public void AESEncryptionShouldReturnNull() throws Exception {
        // When
        byte[] result = crypto.AESEncrypt(null, aesKey);

        // Then
        Assert.assertNull(result);
    }

    @Test
    public void AESDecryptionShouldSucceed() throws Exception {
        // Given
        String toDecrypt = "lBTaVm9kUS4QKnwiklUNWP2PEdioELFF6FbMsJwT7CA=";

        // When
        byte[] decrypted = crypto.AESDecrypt(Base64.decodeBase64(toDecrypt), aesKey);
        String decryptedString = new String(decrypted, Crypto.UTF8_CHARSET);

        // Then
        Assert.assertEquals("String to encrypt", decryptedString);
    }

    @Test
    public void AESDecryptionShouldReturnNull() throws Exception {
        // When
        byte[] result = crypto.AESDecrypt(null, aesKey);

        // Then
        Assert.assertNull(result);
    }

    @Test(expected = GeneralSecurityException.class)
    public void AESDecryptionWithInvalidKeyShouldFail() throws Exception {
        // Given
        String stringToEncrypt = "Another secret";

        // When
        byte[] encrypted = crypto.AESEncrypt(stringToEncrypt, aesKey);
        SecretKey invalidKey = crypto.generateRandomAES128();
        crypto.AESDecrypt(encrypted, invalidKey);
    }

    @Test
    public void generatedAESKeyShouldEncryptDecryptable() throws Exception {
        // Given
        SecretKey generatedKey = crypto.generateRandomAES128();

        // When
        byte[] encrypted = crypto.RSAEncrypt(generatedKey.getEncoded(), rsaPublicKey);
        byte[] decrypted = crypto.RSADecrypt(encrypted, rsaPrivateKey);

        // Then
        Assert.assertEquals(Base64.encodeBase64String(generatedKey.getEncoded()),
                Base64.encodeBase64String(decrypted));
    }

    private byte[] toBytes(String string) throws UnsupportedEncodingException {
        return string.getBytes(Crypto.UTF8_CHARSET);
    }
}
