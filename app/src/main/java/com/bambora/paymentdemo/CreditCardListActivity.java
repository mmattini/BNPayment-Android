package com.bambora.paymentdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.managers.CreditCardManager.IOnCreditCardRead;
import com.bambora.nativepayment.models.creditcard.CreditCard;

import java.util.ArrayList;
import java.util.List;

public class CreditCardListActivity extends AppCompatActivity implements IOnCreditCardRead, OnItemClickListener {

    private List<CreditCard> creditCards = new ArrayList<>();
    private CreditCardListAdapter creditCardListAdapter;
    private BNPaymentHandler paymentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paymentHandler = BNPaymentHandler.getInstance();
        initCreditCardList();
        paymentHandler.getRegisteredCreditCards(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        paymentHandler.getRegisteredCreditCards(this, this);
    }

    private void initCreditCardList() {
        ListView listView = (ListView) findViewById(R.id.lv_credit_cards);
        creditCardListAdapter = new CreditCardListAdapter();
        listView.setOnItemClickListener(this);
        listView.setAdapter(creditCardListAdapter);
    }

    @Override
    public void onCreditCardRead(List<CreditCard> creditCards) {
        if (creditCards != null) {
            this.creditCards = creditCards;
        } else {
            this.creditCards.clear();
        }
        creditCardListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CreditCardDetailsActivity.class);
        CreditCard item = (CreditCard) creditCardListAdapter.getItem(position);
        intent.putExtra(CreditCardDetailsActivity.EXTRA_CREDIT_CARD, item);
        startActivity(intent);
    }

    public class CreditCardListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return creditCards.size();
        }

        @Override
        public Object getItem(int position) {
            return creditCards.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(CreditCardListActivity.this).inflate(R.layout.item_credit_card, null);
                viewHolder = new ViewHolder();
                viewHolder.cardTextView = (TextView) convertView.findViewById(R.id.tv_card_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            CreditCard item = (CreditCard) getItem(position);
            viewHolder.cardTextView.setText(item.getAlias() != null ? item.getAlias() : item.getTruncatedCardNumber());
            return convertView;
        }
    }

    private class ViewHolder {
        TextView cardTextView;
    }
}
