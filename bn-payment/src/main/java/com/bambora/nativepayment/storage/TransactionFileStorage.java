package com.bambora.nativepayment.storage;

import android.content.Context;

import com.bambora.nativepayment.models.creditcard.CreditCard;

/**
 * Extension of {@link FileStorage} for handling transaction lib specific files
 *
 * @author Lovisa Corp
 */
public class TransactionFileStorage extends FileStorage {

    private static final String CREDIT_CARD_FILE_NAME = "credit-card";

    /**
     * Saves an array of {@link CreditCard}s to disk
     * @param context        The context to be used
     * @param creditCards    Array of credit cards to be stored
     */
    public void saveCreditCards(Context context, CreditCard[] creditCards, IOnObjectSaved listener) {
        saveObjectToDisk(context, CREDIT_CARD_FILE_NAME, creditCards, listener);
    }

    public void saveCreditCards(Context context, CreditCard[] creditCards) {
        saveCreditCards(context, creditCards, null);
    }

    /**
     * Retrieves an array of all stored {@link CreditCard}s from disk and notifies result listener
     * when completed. Returns null if no list is found.
     * @param context     The context to be used
     * @param listener    The result listener
     */
    public void getCreditCards(Context context, IOnObjectRead listener) {
        getObjectFromDisk(context, CREDIT_CARD_FILE_NAME, listener);
    }
}
