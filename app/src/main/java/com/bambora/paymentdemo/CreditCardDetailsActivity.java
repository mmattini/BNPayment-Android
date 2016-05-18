package com.bambora.paymentdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.managers.CreditCardManager.IOnCreditCardDeleted;
import com.bambora.nativepayment.models.creditcard.CreditCard;

import java.io.Serializable;

/**
 * @author Lovisa Corp
 */
public class CreditCardDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_CREDIT_CARD = "credit-card-extra";

    private CreditCard creditCard;
    private BNPaymentHandler paymentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paymentHandler = BNPaymentHandler.getInstance();
        creditCard = getCreditCardExtra();
        if (creditCard != null) {
            showCreditCardDetails(creditCard);
        }
    }

    private void showCreditCardDetails(CreditCard creditCard) {
        TextView aliasTextView = (TextView) findViewById(R.id.tv_alias);
        TextView cardNumberTextView = (TextView) findViewById(R.id.tv_card_number);

        aliasTextView.setText(creditCard.getAlias() != null ? creditCard.getAlias() : "No alias");
        cardNumberTextView.setText(creditCard.getTruncatedCardNumber());
    }

    private CreditCard getCreditCardExtra() {
        Serializable creditCardExtra = getIntent().getSerializableExtra(EXTRA_CREDIT_CARD);
        if (creditCardExtra instanceof CreditCard) {
            return (CreditCard) creditCardExtra;
        } else {
            return null;
        }
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_set_alias:
                showSetAliasDialog();
                break;

            case R.id.btn_delete:
                showDeleteDialog();
                break;
        }
    }

    private void updateCreditCard(String aliasInput) {
        creditCard.setAlias(aliasInput);
        showCreditCardDetails(creditCard);
        paymentHandler.setCreditCardAlias(this, aliasInput, creditCard.getCreditCardToken(), null);
    }

    private void deleteCreditCard() {
        paymentHandler.deleteCreditCard(this, creditCard.getCreditCardToken(), new IOnCreditCardDeleted() {
            @Override
            public void onCreditCardDeleted() {
                finish();
                Toast.makeText(CreditCardDetailsActivity.this, "Card successfully deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSetAliasDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        FrameLayout inputView = new FrameLayout(this);
        inputView.setPadding(70, 100, 70, 50);
        final EditText editText = new EditText(this);
        editText.setInputType(
                        InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_FLAG_CAP_SENTENCES |
                        InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        inputView.addView(editText);
        alertBuilder.setView(inputView);
        alertBuilder.setTitle("Enter an alias");
        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String aliasInput = editText.getText().toString();
                updateCreditCard(aliasInput);
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.show();
    }

    private void showDeleteDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Are you sure you want to delete this card?");
        alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                deleteCreditCard();
            }
        });
        alertBuilder.setNegativeButton("Cancel", null);
        alertBuilder.show();
    }
}
