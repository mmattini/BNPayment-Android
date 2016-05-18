package com.bambora.nativepayment.interfaces;

/**
 * {@link ITransactionListener} is an interface defining the the callback method
 * for a transaction operation.
 *
 * Created by oskarhenriksson on 16/10/15.
 */
public interface ITransactionListener {

    enum TransactionResult {
        TRANSACTION_RESULT_SUCCESS,
        TRANSACTION_RESULT_FAILURE
    }
    /**
     * A callback method executed after a transaction operation is finished.
     */
    void onTransactionResult(TransactionResult result);
}
