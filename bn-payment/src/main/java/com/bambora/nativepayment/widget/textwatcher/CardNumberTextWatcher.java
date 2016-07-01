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

package com.bambora.nativepayment.widget.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;

import com.bambora.nativepayment.widget.CardInputValidator;
import com.bambora.nativepayment.widget.CardNumberFormat;
import com.bambora.nativepayment.widget.FormInputHelper;

import static com.bambora.nativepayment.widget.CardNumberFormat.CardType.MASTERCARD;
import static com.bambora.nativepayment.widget.CardNumberFormat.CardType.VISA;

/**
 * TODO
 */
public class CardNumberTextWatcher implements TextWatcher {

    private static final char SPACE = ' ';

    private CardNumberFormat[] validFormats = {
            new CardNumberFormat(VISA, "^4[0-9]{12}(?:[0-9]{3})?$", "^4\\d*", CardNumberFormat.FORMAT_GROUP_BY_4),
            new CardNumberFormat(MASTERCARD, "^5[1-5][0-9]{14}$", "^5[1-5]\\d*", CardNumberFormat.FORMAT_GROUP_BY_4)
    };

    private CardNumberFormat numberFormat = CardNumberFormat.defaultFormat();
    private boolean deleting;
    private CharSequence deleted;
    private int startIndex;

    private CardInputValidator validator;
    private CardTypeListener cardTypeListener;

    public CardNumberTextWatcher(CardInputValidator inputFormatter, CardTypeListener cardTypeListener) {
        this.validator = inputFormatter;
        this.cardTypeListener = cardTypeListener;
        validator.setFormatPattern(numberFormat.getFormatString());
        validator.setValidationPattern(numberFormat.getValidationString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        deleting = after < count;
        deleted = deleting ? FormInputHelper.getDeletedChars(s, start, count) : "";
        startIndex = start;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        numberFormat = guessCardNumberFormat(s);
    }

    @Override
    public void afterTextChanged(Editable s) {
        validator.setFormatPattern(numberFormat.getFormatString());
        validator.setValidationPattern(numberFormat.getValidationString());
        if (deleting && deleted.equals(String.valueOf(SPACE)) && s.length() > 0) {
            s.delete(startIndex -1, startIndex);
        }
        if (!validator.isFormatted()) {
            CharSequence formatted = FormInputHelper.formatNumberSequence(s.toString(), 4, SPACE);
            s.replace(0, s.length(), formatted);
        }
    }

    private CardNumberFormat guessCardNumberFormat(CharSequence input) {
        CardNumberFormat newFormat = CardNumberFormat.defaultFormat();
        for (CardNumberFormat format : validFormats) {
            if (format.possibleMatch(input)) {
                newFormat = format;
                break;
            }
        }
        notifyIfTypeChanged(newFormat);
        return newFormat;
    }

    private void notifyIfTypeChanged(CardNumberFormat newFormat) {
        if (cardTypeListener == null) return;
        if (numberFormat != null && newFormat.getCardType() != numberFormat.getCardType()) {
           cardTypeListener.onCardTypeChanged(newFormat);
        }
    }

    public interface CardTypeListener {
        void onCardTypeChanged(CardNumberFormat cardNumberFormat);
    }
}
