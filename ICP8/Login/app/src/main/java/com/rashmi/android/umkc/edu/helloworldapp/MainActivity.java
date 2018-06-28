package com.rashmi.android.umkc.edu.helloworldapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    private static EditText username;
    private static EditText password;

    private static Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginButton();

    }

    private void LoginButton() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.button);

    }



    public void sign_in(View v) {

            if (username.getText().toString().equals("admin") &&
                password.getText().toString().equals("admin")) {
            //Toast.makeText(this, "Username and password is correct",
                    //Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,user_page.class);

                startActivity(intent);



            } else {
            Toast.makeText(this, "Username and password is NOT correct",
                    Toast.LENGTH_SHORT).show();


            button.setEnabled(false);
            }

    }



}

