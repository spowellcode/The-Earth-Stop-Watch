package com.example.earthstopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {

    private AdView mAdView;
    private TextView milestextview;
    private TextView secTextView;
    private Button StartStopbtn;
    private Button Reset;
    private Handler mHandler;

    PrimeThread p = new PrimeThread(); //Creates object for thread class which calculates time variable
    SecondThread secThread = new SecondThread(); //Creates object of a thread class which calculate how far the Earth has moved since the timer started.

    boolean ThreadRun = false; //True = Thread is running. False = Thread is not running.
    private double count = 0; //Variable for timer/seconds
    private double miles = 0; //Variable for miles

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        milestextview = findViewById(R.id.MilesTextView);
        mAdView = findViewById(R.id.adView);
        StartStopbtn = findViewById(R.id.StartStopbtn);
        Reset = findViewById(R.id.ResetBtn);
        secTextView = findViewById(R.id.secTextView);
        mHandler = new Handler();

        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111"); //Test Ad ID

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        StartStopbtn.setOnClickListener(new View.OnClickListener() { //Listens for start button UI
            @Override
            public void onClick(View v) {
                if (!ThreadRun) { //If start button is pressed
                    StartStopbtn.setText("Stop"); //Updates UI. Changes Start button to Stop
                    new Thread(p).start(); //Starts time thread
                    new Thread(secThread).start(); //Starts miles thread
                    ThreadRun = true; //Enables thread to run multiple times
                }
                if (ThreadRun) { //If Stop button is pressed
                    ThreadRun = false; //Stops threads
                    StartStopbtn.setText("Start"); //Stop button text to Start
                }
            }

        });
        Reset.setOnClickListener(new View.OnClickListener(){ //Listens for reset button on UI
            @Override
            public void onClick(View v) {
                ThreadRun = false;
                count = 0; //Resets timer
                miles = 0; //Resets miles
                milestextview.setText(valueOf(miles)); //Updates miles on UI
                secTextView.setText(valueOf(count)); //Updates timer on UI
                StartStopbtn.setText("Start"); //Changes Stop button text to start
            }
        });


    }
    class PrimeThread extends Thread {

        public void run() {
            do {
                ThreadRun = true; //Boolean for if the thread is supposed to run or not
                count=count+.5; //adds half a second

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        secTextView.setText(valueOf(count)); //Sets Text view to how much time has passed.
                        StartStopbtn.setText("Stop"); //Sets start button text to 'Stop'

                    }
                });
                try {
                    Thread.sleep(500); //Seconds move in 500 millisecond increments
                } catch (InterruptedException e) { //Catches error in the event of
                    e.printStackTrace(); //Prints error in the event of
                }
            }while (ThreadRun); //Loop runs while ThreadRun is true
            ThreadRun = false; //Stops threads in app
        }
    }
    class SecondThread extends Thread { //Class for miles counter thread

        public void run() {
            do {
                ThreadRun = true; // Starts thread
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        milestextview.setText(valueOf(miles)); //Sets miles text to miles variables
                    }
                });
                try {
                    Thread.sleep(1000); //Waits for the earth to move in for second
                } catch (InterruptedException e) { //Catches error in the event of
                    e.printStackTrace(); //Prints error in the event of
                }
                miles=miles+18.5; //Earth moves roughly 18.5 miles a second (-;
            }while (ThreadRun); //Loop runs while ThreadRun is true
            ThreadRun = false; //Stops threads in app
        }
    }
}
