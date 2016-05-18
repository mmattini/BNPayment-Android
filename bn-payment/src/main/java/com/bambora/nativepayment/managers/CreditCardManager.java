package com.bambora.nativepayment.managers;

import android.content.Context;

import com.bambora.nativepayment.storage.FileStorage;
import com.bambora.nativepayment.storage.FileStorage.IOnObjectSaved;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.storage.TransactionFileStorage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for fetching and storing credit cards from local storage.
 */
public class CreditCardManager {

    private TransactionFileStorage fileStorage = new TransactionFileStorage();

    /**
     * Adds a credit card to existing stored credit cards.
     * @param context    App Context object
     * @param creditCard Credit card object to be stored
     */
    public void saveCreditCard(final Context context, final CreditCard creditCard, final IOnCreditCardSaved onSavedListener) {
        getCreditCards(context, new IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> storedCreditCards) {
                int location = getLocationInList(storedCreditCards, creditCard);
                if (location >= 0) {
                    // If the card already exists, replace it
                    storedCreditCards.remove(location);
                    storedCreditCards.add(location, creditCard);
                } else {
                    storedCreditCards.add(creditCard);
                }
                OnCardSavedNotifier onCardSaved = new OnCardSavedNotifier(creditCard, onSavedListener);
                fileStorage.saveCreditCards(context, toArray(storedCreditCards), onCardSaved);
            }
        });
    }

    /**
     * Reads all stored credit cards from local storage asynchronously and notifies listener.
     * @param context  App Context object
     * @param listener Callback listener; called when list of cards have been loaded
     */
    public void getCreditCards(Context context, final IOnCreditCardRead listener) {
        if (listener == null) {
            return;
        }
        fileStorage.getCreditCards(context, new FileStorage.IOnObjectRead() {
            @Override
            public void notifyOnObjectRead(Serializable object) {
                CreditCard[] creditCards = object instanceof CreditCard[] ? (CreditCard[]) object : new CreditCard[0];
                listener.onCreditCardRead(new ArrayList<>(Arrays.asList(creditCards)));
            }
        });
    }

    /**
     * Deletes a stored credit card with given token from local disk.
     * @param context           App context object
     * @param creditCardToken   Token for credit card to be deleted
     * @param listener          Result listener
     */
    public void deleteCreditCard(final Context context, final String creditCardToken, final IOnCreditCardDeleted listener) {
        final IOnObjectSaved onObjectSavedListener = new IOnObjectSaved() {
            @Override
            public void notifyOnObjectSaved() {
                if (listener != null) {
                    listener.onCreditCardDeleted();
                }
            }
        };
        getCreditCards(context, new IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> creditCards) {
                List<CreditCard> newList = removeFromList(creditCards, creditCardToken);
                fileStorage.saveCreditCards(context, toArray(newList), onObjectSavedListener);
            }
        });
    }

    /**
     * Updates credit card alias for card with given token.
     * @param context   App Context object
     * @param alias     Name/Alias for the card
     * @param token     Credit card token
     * @param listener  Status listener. Called when update is completed.
     */
    public void setAlias(final Context context, final String alias, final String token, final IOnCreditCardSaved listener) {
        getCreditCards(context, new IOnCreditCardRead() {
            @Override
            public void onCreditCardRead(List<CreditCard> creditCards) {
                CreditCard creditCard = findCreditCardInList(creditCards, token);
                if (creditCard != null) {
                    creditCard.setAlias(alias);
                    OnCardSavedNotifier onCardUpdated = new OnCardSavedNotifier(creditCard, listener);
                    fileStorage.saveCreditCards(context, toArray(creditCards), onCardUpdated);
                }
            }
        });
    }

    /**
     * Checks if given {@link CreditCard} exists in given array
     * @param listToSearch  Credit card array to check
     * @param cardToFind    Credit card to find
     * @return True if card exists in array
     */
    private boolean existsIn(List<CreditCard> listToSearch, CreditCard cardToFind) {
        for (CreditCard card : listToSearch) {
            if (card.isEqualTo(cardToFind)) {
                return true;
            }
        }
        return false;
    }

    private CreditCard[] toArray(List<CreditCard> creditCardList) {
        CreditCard[] creditCardArray = new CreditCard[creditCardList.size()];
        return creditCardList.toArray(creditCardArray);
    }

    private List<CreditCard> removeFromList(List<CreditCard> creditCardList, String token) {
        Iterator<CreditCard> iterator = creditCardList.iterator();
        while (iterator.hasNext()) {
            CreditCard creditCard = iterator.next(); // must be called before you can call i.remove()
            if (creditCard.getCreditCardToken().equals(token)) {
                iterator.remove();
            }
        }
        return creditCardList;
    }

    private int getLocationInList(List<CreditCard> creditCardList, CreditCard creditCard) {
        for (int location = 0; location < creditCardList.size(); location++) {
            if (creditCardList.get(location).isEqualTo(creditCard)) {
                return location;
            }
        }
        return -1;
    }

    private CreditCard findCreditCardInList(List<CreditCard> creditCardList, String token) {
        for (CreditCard creditCard : creditCardList) {
            if (creditCard.getCreditCardToken().equals(token)) {
                return creditCard;
            }
        }
        return null;
    }

    /**
     * Interface for callback listener when loading credit card from local storage
     */
    public interface IOnCreditCardRead {
        void onCreditCardRead(List<CreditCard> creditCards);
    }

    /**
     * Interface for callback listener when saving/updating a credit card. Called when task is
     * completed.
     */
    public interface IOnCreditCardSaved {
        void onCreditCardSaved(CreditCard creditCard);
    }

    /**
     * Interface for callback listener when deleting a credit card. Called when task is
     * completed.
     */
    public interface IOnCreditCardDeleted {
        void onCreditCardDeleted();
    }


    /**
     * Helper class for notifying listener when a {@link CreditCard} has been saved. The notifier
     * returns the card that was saved.
     */
    private class OnCardSavedNotifier implements IOnObjectSaved {
        private CreditCard creditCard;
        private IOnCreditCardSaved onCreditCardSaved;

        public OnCardSavedNotifier(CreditCard creditCard, IOnCreditCardSaved onCreditCardSaved) {
            this.creditCard = creditCard;
            this.onCreditCardSaved = onCreditCardSaved;
        }

        @Override
        public void notifyOnObjectSaved() {
            if (onCreditCardSaved != null) {
                onCreditCardSaved.onCreditCardSaved(this.creditCard);
            }
        }
    }
}
