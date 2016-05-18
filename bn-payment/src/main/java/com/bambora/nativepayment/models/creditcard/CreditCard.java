package com.bambora.nativepayment.models.creditcard;

import java.io.Serializable;

/**
 * @author Lovisa Corp
 */
public class CreditCard implements Serializable {

    /**
     * Alias of the card
     */
    private String alias;

    /**
     * Truncated credit card number
     */
    private String truncatedCardNumber;

    /**
     * Card expiry month
     */
    private Integer expiryMonth;

    /**
     * Card expiry year
     */
    private Integer expiryYear;

    /**
     * Payment type, such as Visa or MasterCard
     */
    private String paymentType;

    /**
     * ID of transaction created at the time of card registration
     */
    private String transactionId;

    /**
     * Token to be used when making transactions
     */
    private String creditCardToken;

    public CreditCard(String truncatedCardNumber, Integer expiryMonth, Integer expiryYear,
                      String paymentType, String transactionId, String creditCardToken) {
        this.truncatedCardNumber = truncatedCardNumber;
        this.expiryMonth = expiryMonth;
        this.expiryYear = expiryYear;
        this.paymentType = paymentType;
        this.transactionId = transactionId;
        this.creditCardToken = creditCardToken;
    }

    public CreditCard(RegistrationResult registrationResult) {
        if (registrationResult != null) {
            this.truncatedCardNumber = registrationResult.truncatedCardNumber;
            this.expiryMonth = registrationResult.expiryMonth;
            this.expiryYear = registrationResult.expiryYear;
            this.paymentType = registrationResult.paymentType;
            this.transactionId = registrationResult.transactionId;
            this.creditCardToken = registrationResult.subscriptionId;
        }
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTruncatedCardNumber() {
        return truncatedCardNumber;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCreditCardToken() {
        return creditCardToken;
    }

    /**
     * Checks if given {@link CreditCard} has the same token as this
     * @param otherCard Credit card to compare with
     * @return          True if the tokens are equal
     */
    public boolean isEqualTo(CreditCard otherCard) {
        return otherCard.getCreditCardToken().equals(this.creditCardToken);
    }
}
