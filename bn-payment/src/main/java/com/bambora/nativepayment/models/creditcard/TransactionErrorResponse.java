package com.bambora.nativepayment.models.creditcard;

/**
 * @author Lovisa Corp
 */
public class TransactionErrorResponse {

    public final Integer status;
    public final Integer errorCode;
    public final String errorMessage;
    public final String errorInfo;

    public TransactionErrorResponse(Integer status, Integer errorCode, String errorMessage, String errorInfo) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorInfo = errorInfo;
    }
}
