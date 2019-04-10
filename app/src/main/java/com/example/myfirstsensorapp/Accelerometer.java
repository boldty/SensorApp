package com.example.myfirstsensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity implements SensorEventListener{

    MediaPlayer mpL,mpR;
    Vibrator vibrator;
    boolean haveL, haveR;
    private TextView xText,yText, zText, oText;
    private Sensor mySensor;
    private SensorManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_event);

        // Creates the sensor manager
        sm = (SensorManager)getSystemService(SENSOR_SERVICE);

        // Acceleromter sensor
        mySensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Assign TextViews
        xText = (TextView)findViewById(R.id.xText);
        yText = (TextView)findViewById(R.id.yText);
        zText = (TextView)findViewById(R.id.zText);

        oText = (TextView)findViewById(R.id.oText);

        // Assign other values

        haveL = false;
        haveR = false;
        mpR = MediaPlayer.create(this, R.raw.right);
        mpL = MediaPlayer.create(this, R.raw.left);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Registers sensor listener
        start();

    }

    @Override
    public void onSensorChanged(android.hardware.SensorEvent event) {
        float x = event.values[0];

        xText.setText("X: " + String.format("%.2f",x));
        yText.setText("Y: " + String.format("%.2f",event.values[1]));
        zText.setText("Z: " + String.format("%.2f",event.values[2]));

        if(x >= 4){
            oText.setText("Left");
            if(!haveL){
                mpL.start();
                vibrate();
                haveR = false;
                haveL = true;
            }
        }
        else if(x <= -4){
            oText.setText("Right");
            if(!haveR){
                mpR.start();
                vibrate();
                haveL = false;
                haveR = true;
            }
        }
        else{
            haveL = false;
            haveR = false;
            oText.setText("Upright");
        }

    }

    private void vibrate(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            vibrator.vibrate(500);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Not used in this application
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    public void start() {
        sm.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop() {
        sm.unregisterListener(this, mySensor);
    }
}













