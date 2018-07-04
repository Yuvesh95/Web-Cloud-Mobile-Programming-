package com.example.karthik.myorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);



    }

    public void sendSummary(android.view.View v){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "shalin@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order Summary");

        intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(name,hasCheese,haschicken,hasCream,total_price));
        startActivity(Intent.createChooser(intent, ""));
    }

}
