// Student Name: Quang Tran (ID: 217 078 205)
// This lab was done individually.

package com.example.mcalcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import ca.roumani.i2c.MPro;

public class MCalcPro_Activity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.tts = new TextToSpeech(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mcalcpro_layout);
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }
    public void onSensorChanged(SensorEvent event) {
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];
        double a = Math.sqrt(ax * ax + ay * ay + az * az);
        if (a > 20) {
            ((EditText) findViewById(R.id.pBox)).setText("");
            ((EditText) findViewById(R.id.aBox)).setText("");
            ((EditText) findViewById(R.id.iBox)).setText("");
            ((TextView) findViewById(R.id.output)).setText("");
        }
    }

    public void buttonClicked(View v) {
        try {
            String principle = ((EditText) findViewById(R.id.pBox)).getText().toString();
            String amortization = ((EditText) findViewById(R.id.aBox)).getText().toString();
            String interest = ((EditText) findViewById(R.id.iBox)).getText().toString();

            MPro mp = new MPro();
            mp.setPrinciple(principle);
            mp.setAmortization(amortization);
            mp.setInterest(interest);


            // configure the result message
            String s = "Monthly Payment = $" + mp.computePayment("%,.2f");
            s += "\n\n";
            s += "By making this payments monthly for " + amortization + " years, the mortgage will be " +
                    "paid in full. However, if you terminate the mortgage on its n th anniversary, "
                    + "the balance still owing depends on n as described below:";
            s += "\n\n";
            s += String.format("%8s %16s", "n", "Balance");
            s += "\n\n";
            s += String.format("%8d", 0) + mp.outstandingAfter(0, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 1) + mp.outstandingAfter(1, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 2) + mp.outstandingAfter(2, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 3) + mp.outstandingAfter(3, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 4) + mp.outstandingAfter(4, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 5) + mp.outstandingAfter(5, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 10) + mp.outstandingAfter(10, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 15) + mp.outstandingAfter(15, "%,16.0f");
            s += "\n\n";
            s += String.format("%8d", 20) + mp.outstandingAfter(20, "%,16.0f");
            s += "\n\n";
            ((TextView) findViewById(R.id.output)).setText(s);

            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
        catch (Exception e) {
            Toast label = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            label.show();
        }
    }

    public void onInit(int initStatus) {
        this.tts.setLanguage(Locale.US);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        tts.shutdown();
    }
}