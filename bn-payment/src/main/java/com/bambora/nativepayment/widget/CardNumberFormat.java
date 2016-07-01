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

import java.util.regex.Pattern;

import static com.bambora.nativepayment.widget.CardNumberFormat.CardType.UNKNOWN;

/**
 * TODO
 */
public class CardNumberFormat {

    public static final String DEFAULT_VALIDATION_PATTERN =
            "^(?:4[0-9]{12}(?:[0-9]{3})?" +         // Visa
            "|(?:5[1-5][0-9]{2}" +               // MasterCard
            "|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}" +
            "|3[47][0-9]{13}" +                  // American Express
            "|3(?:0[0-5]|[68][0-9])[0-9]{11}" +  // Diners Club
            "|6(?:011|5[0-9]{2})[0-9]{12}" +     // Discover
            "|(?:2131|1800|35\\d{3})\\d{11}" +   // JCB
            ")$";
    public static final String FORMAT_GROUP_BY_4 = "((\\d{4} )*((\\d{4} )|(\\d{1,4})))?";

    public static final String VISA_NUMBER_IDENTIFIER = "^4\\d*";
    public static final String VISA_NUMBER_FORMAT = "^4[0-9]{12}(?:[0-9]{3})?$";
    public static final String VISA_NUMBER_VIEW_FORMAT = "((\\d{4} )*((\\d{4} )|(\\d{1,4})))?";

    public static final String MC_NUMBER_IDENTIFIER = "^5[1-5]\\d*";
    public static final String MC_NUMBER_FORMAT = "^5[1-5][0-9]{14}$";
    public static final String MC_NUMBER_VIEW_FORMAT = "((\\d{4} )*((\\d{4} )|(\\d{1,4})))?";

    public enum CardType {
        VISA,
        MASTERCARD,
        AMERICAN_EXPRESS,
        UNKNOWN
    }

    private CardType cardType;

    private String validNumberPattern;
    private Pattern cardTypePattern;
    private String formatPattern;
    public CardNumberFormat(CardType cardType, String validNumberPattern, String cardTypePattern, String formatPattern) {
        this.cardType = cardType;
        this.validNumberPattern = validNumberPattern;
        if (cardTypePattern != null)
            this.cardTypePattern = Pattern.compile(cardTypePattern);
        this.formatPattern = formatPattern;
    }

    public static CardNumberFormat defaultFormat() {
        return new CardNumberFormat(UNKNOWN, DEFAULT_VALIDATION_PATTERN, null, FORMAT_GROUP_BY_4);
    }

    public CardType getCardType() {
        return cardType;
    }

    public Integer getIconResId() {
        switch (cardType) {
            case MASTERCARD:
                return R.drawable.ic_master_card;
            case VISA:
                return R.drawable.ic_visa;
            default:
                return null;
        }
    }

    public boolean possibleMatch(CharSequence input) {
        String stripped = stripFromDigits(input);
        return cardTypePattern.matcher(stripped).matches();
    }

    public String getValidationString() {
        return validNumberPattern;
    }

    public String getFormatString() {
        return formatPattern;
    }

    private String stripFromDigits(CharSequence input) {
        return String.valueOf(input).replaceAll("[^\\d]", "");
    }
}
