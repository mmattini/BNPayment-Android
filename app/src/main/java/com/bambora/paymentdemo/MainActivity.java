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

package com.bambora.paymentdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.handlers.BNPaymentHandler.BNPaymentBuilder;
import com.bambora.nativepayment.interfaces.ITransactionListener;
import com.bambora.nativepayment.managers.CreditCardManager;
import com.bambora.nativepayment.models.PaymentSettings;
import com.bambora.nativepayment.models.creditcard.CreditCard;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String API_TOKEN = "<Replace with your token>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Setup BNPaymentHandler
        BNPaymentBuilder BNPaymentBuilder = new BNPaymentBuilder(getApplicationContext(),
                API_TOKEN)
                .debug(true);

        BNPaymentHandler.setupBNPayments(BNPaymentBuilder);
        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        Button hppButton = (Button) findViewById(R.id.hpp_button);
        hppButton.setOnClickListener(mHppButtonListener);

        Button nativeRegistrationButton = (Button) findViewById(R.id.native_registration_button);
        nativeRegistrationButton.setOnClickListener(mNativeRegistrationButtonListener);

        Button makeTransactionButton = (Button) findViewById(R.id.make_transaction_button);
        makeTransactionButton.setOnClickListener(mMakeTransactionListener);

        Button listCreditCardsButton = (Button) findViewById(R.id.list_credit_cards_button);
        listCreditCardsButton.setOnClickListener(mListCreditCardsListener);
    }

    private void showHostedPaymentPage() {
        Intent intent = new Intent(this, HostedPaymentPageActivity.class);
        startActivity(intent);
    }

    private void showNativeCardRegistration() {
        Intent intent = new Intent(this, NativeCardRegistrationActivity.class);
        startActivity(intent);
    }

    private void makeTransaction(final PaymentSettings paymentSettings) {
        String paymentId = "test-payment-" + new Date().getTime();
//        String paymentId = "123456";
        BNPaymentHandler.getInstance().makeTransaction(paymentId, paymentSettings, new ITransactionListener() {
            @Override
            public void onTransactionResult(TransactionResult result) {
                switch (result) {
                    case TRANSACTION_RESULT_SUCCESS:
                        showDialog("Success", "The payment succeeded.");
                        break;

                    default:
                        showDialog("Failure", "The payment did not succeed.");
                        break;
                }
            }
        });
    }

    private void listCreditCards() {
        Intent intent = new Intent(this, CreditCardListActivity.class);
        startActivity(intent);
    }

    private void showDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    Button.OnClickListener mHppButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showHostedPaymentPage();
        }
    };

    Button.OnClickListener mNativeRegistrationButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showNativeCardRegistration();
        }
    };

    Button.OnClickListener mMakeTransactionListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BNPaymentHandler.getInstance().getRegisteredCreditCards(MainActivity.this, new CreditCardManager.IOnCreditCardRead() {
                @Override
                public void onCreditCardRead(List<CreditCard> creditCards) {
                    if (creditCards != null && creditCards.size() > 0) {
                        PaymentSettings paymentSettings = new PaymentSettings();
                        paymentSettings.amount = 100;
                        paymentSettings.currency = "SEK";
                        paymentSettings.comment = "This is a test transaction.";
                        paymentSettings.creditCardToken = creditCards.get(0).getCreditCardToken();
                        makeTransaction(paymentSettings);
                    } else {
                        showDialog("No credit card registered", "Please register a credit card in order to make a purchase.");
                    }
                }
            });
        }
    };

    Button.OnClickListener mListCreditCardsListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listCreditCards();
        }
    };
}
