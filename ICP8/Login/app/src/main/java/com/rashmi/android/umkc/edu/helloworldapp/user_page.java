package com.rashmi.android.umkc.edu.helloworldapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

/**
 * Created by Yuvesh on 6/28/2018.
 */

public class user_page extends AppCompatActivity {

    private static Button logout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_page);

}

    public void logout (View v) {

        Intent intent = new Intent(user_page.this,MainActivity.class);
        startActivity(intent);


    }
    }

