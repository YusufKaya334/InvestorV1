package com.example.investor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class Loading_Screen extends AppCompatActivity {
    ProgressBar progressBar;
    int count=0;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        progressBar=findViewById(R.id.progressBar);
        timer=new Timer();
        TimerTask  timerTask=new TimerTask() {
            @Override
            public void run() {
                count++;
                progressBar.setProgress(count);
                if(count==100){
                    timer.cancel();
                    Intent intent=new Intent(Loading_Screen.this,Options_menu.class);
                    startActivity(intent);
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }
}