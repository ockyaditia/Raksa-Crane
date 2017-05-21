package com.example.ockyaditiasaputra.raksacrane;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;

import java.util.Random;

public class SplashScreen extends AppCompatActivity {

    private static int splashInterval = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);

        int[] mArray = new int[10];
        mArray[0] = R.drawable.w1_splash;
        mArray[1] = R.drawable.w2_splash;
        mArray[2] = R.drawable.w3_splash;
        mArray[3] = R.drawable.w4_splash;
        mArray[4] = R.drawable.w5_splash;
        mArray[5] = R.drawable.w6_splash;
        mArray[6] = R.drawable.w7_splash;
        mArray[7] = R.drawable.w8_splash;
        mArray[8] = R.drawable.w9_splash;
        mArray[9] = R.drawable.w10_splash;

        Random random = new Random();

        int r = random.nextInt(9);

        ImageView splashScreen = (ImageView) findViewById(R.id.splashScreen);
        splashScreen.setImageResource(mArray[r]);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                this.finish();
            }

            private void finish() {
                // TODO Auto-generated method stub
            }
        }, splashInterval);
    }
}
