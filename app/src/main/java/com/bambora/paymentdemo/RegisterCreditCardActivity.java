package com.bambora.paymentdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.nativepayment.models.creditcard.RegistrationFormError;
import com.bambora.nativepayment.webview.CreditCardRegistrationWebView;

public class RegisterCreditCardActivity extends AppCompatActivity implements CreditCardRegistrationWebView.IStateChangeListener {

    private static final String FORM_CSS_URL = "http://ci.mobivending.com/CNP/example.css";

    private CreditCardRegistrationWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_credit_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup BNPaymentHandler

        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupView() {
        mWebView = (CreditCardRegistrationWebView) findViewById(R.id.register_credit_card_webview);
        mWebView.setStateChangeListener(this);
        mWebView.setCssUrl(FORM_CSS_URL);
    }

    private void showDialog(String title, String message, final boolean navigateUp) {
        final RegisterCreditCardActivity instance = this;

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (navigateUp) {
                            NavUtils.navigateUpFromSameTask(instance);
                        }

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onPageFinished() {
        Toast.makeText(this, "Page finished loading", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationStarted() {
        Log.d(getClass().getSimpleName(), "Loading started");
        Toast.makeText(this, "Loading started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationCompleted(CreditCard creditCard) {
        Log.d(getClass().getSimpleName(), "Registration completed successfully");
        Toast.makeText(this, "Registration completed successfully", Toast.LENGTH_SHORT).show();
        NavUtils.navigateUpFromSameTask(this);
    }

    @Override
    public void onFailure(RegistrationFormError error) {
        Log.d(getClass().getSimpleName(), "Failure");
        Toast.makeText(this, "Credit card registration failed: " + error, Toast.LENGTH_SHORT).show();
    }
}
