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

package com.bambora.nativepayment.mock;

import android.content.Context;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.storage.TransactionFileStorage;

/**
 * @author Lovisa Corp
 */
public class TransactionFileStorageMock extends TransactionFileStorage {

    private CreditCard[] savedCreditCards = new CreditCard[0];

    @Override
    public void saveCreditCards(Context context, CreditCard[] creditCards, IOnObjectSaved listener) {
        this.savedCreditCards = creditCards;
        if (listener != null) {
            listener.notifyOnObjectSaved();
        }
    }

    @Override
    public void getCreditCards(Context context, IOnObjectRead listener) {
        listener.notifyOnObjectRead(savedCreditCards);
    }

    public int getNrOfCreditCards() {
        return savedCreditCards.length;
    }

    public CreditCard getCreditCard(String token) {
        for (CreditCard creditCard : savedCreditCards) {
            if (creditCard.getCreditCardToken().equals(token)) {
                return creditCard;
            }
        }
        return null;
    }
}
