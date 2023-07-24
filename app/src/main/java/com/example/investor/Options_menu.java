package com.example.investor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Options_menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);
    }
    public void todoviz_ekrani (View view){
        Intent intent=new Intent(Options_menu.this,doviz_ekrani.class);
        startActivity(intent);
    }
    public void togold(View view){
        Intent intent=new Intent(Options_menu.this,gold.class);
        startActivity(intent);
    }

    public void  toBalance(View view){
        Intent intent=new Intent(Options_menu.this,Balance.class);
        startActivity(intent);

    }
}