package com.example.ifood.activity.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

import com.example.ifood.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                irAutenticacao();
            }
        },4000 );
    }

    public void irAutenticacao(){
        Intent i = new Intent(MainActivity.this, AutenticacaoActivity.class);

        startActivity(i);
    }

}
