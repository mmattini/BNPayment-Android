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

package com.bambora.nativepayment.widget;

import com.bambora.nativepayment.R;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.bambora.nativepayment.widget.CardNumberFormat.CardType;
import static com.bambora.nativepayment.widget.CardNumberFormat.CardType.MASTERCARD;
import static com.bambora.nativepayment.widget.CardNumberFormat.CardType.VISA;

/**
 * Instrumented tests for the {@link CardNumberFormat} class.
 */
@RunWith(Parameterized.class)
public class CardNumberFormatTest {

    private CardNumberFormat cardNumberFormat;
    private CharSequence cardNumber;
    private CardType cardType;
    private int maxInputLength;
    private Integer iconResId;
    private List<Integer> formatGroupSizes;

    public CardNumberFormatTest(CharSequence cardNumber, CardType cardType, int maxInputLength, Integer iconResId, List<Integer> formatGroupSizes) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.maxInputLength = maxInputLength;
        this.iconResId = iconResId;
        this.formatGroupSizes = formatGroupSizes;
    }

    @Before
    public void setUp() {
        cardNumberFormat = new CardNumberFormat();
    }

    @After
    public void teardown() {
        cardNumberFormat = null;
    }

    @Parameterized.Parameters
    public static Collection cardTypeInformation() {
        return Arrays.asList(new Object[][] {
                {"4002 6200 0000 0005", VISA, 19, R.drawable.ic_visa, Arrays.asList(4, 4, 4, 4)},
                {"5125 8600 0000 0006", MASTERCARD, 19, R.drawable.ic_master_card, Arrays.asList(4, 4, 4, 4)}
        });
    }

    @Test
    public void testGetCardType() {
        // When
        CardType cardType = cardNumberFormat.getCardType();

        // Then
        Assert.assertTrue(cardType == CardType.UNKNOWN);
    }

    @Test
    public void testGetValidationString() {
        // Given
        String expectedValidationString = "^(?:4[0-9]{12}(?:[0-9]{3})?|(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|6(?:011|5[0-9]{2})[0-9]{12}|(?:2131|1800|35\\d{3})\\d{11})$";

        // When
        String actualValidatonString = cardNumberFormat.getValidationString();

        // Then
        Assert.assertEquals(expectedValidationString, actualValidatonString);
    }

    @Test
    public void testUpdateCardType() {
        // When
        cardNumberFormat.updateCardType(cardNumber);

        // Then
        Assert.assertEquals(cardType, cardNumberFormat.getCardType());
        Assert.assertEquals(maxInputLength, cardNumberFormat.getMaxInputLength());
        Assert.assertEquals(iconResId, cardNumberFormat.getIconResId());
        Assert.assertEquals(formatGroupSizes, cardNumberFormat.getFormatGroupSizes());
    }

    @Test
    public void testUpdateCardTypeWithInvalidCardNumber() {
        // Given
        CharSequence cardNumber = "0";

        // When
        Boolean result = cardNumberFormat.updateCardType(cardNumber);

        // Then
        Assert.assertFalse(result);
        Assert.assertEquals(CardType.UNKNOWN, cardNumberFormat.getCardType());
    }
}
