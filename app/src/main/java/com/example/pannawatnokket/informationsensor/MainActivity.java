package com.example.pannawatnokket.informationsensor;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private static final float SHAKE_THRESHOLD = 1;
    private static final int MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000;
    private long mLastShakeTime;
    private SensorManager mSensorMgr;
    private Animation animShake;
    private Animation stickAnimation;
    private int count;
    private boolean check;
    private Random random;
    private String[] predicts;

    private ImageView semsiImageView;
    private ImageView stickImageView;

    private boolean isDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isDialog = true;
        mSensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            mSensorMgr.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        semsiImageView = (ImageView) findViewById(R.id.semsi);
        stickImageView = (ImageView) findViewById(R.id.stick);

        animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        stickAnimation = AnimationUtils.loadAnimation(this, R.anim.stick);

        semsiImageView.startAnimation(animShake);
        stickImageView.startAnimation(stickAnimation);

        count = 0;
        check = true;
        predicts = Predict.predicts;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if (check) {
                if ((curTime - mLastShakeTime) > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {

                    float x = event.values[0];
                    float y = event.values[1];
                    float z = event.values[2];

                    double acceleration = Math.sqrt(Math.pow(x, 2) +
                            Math.pow(y, 2) +
                            Math.pow(z, 2)) - SensorManager.GRAVITY_EARTH;


                    if (acceleration > SHAKE_THRESHOLD) {
                        mLastShakeTime = curTime;
                        if (isDialog) {
                            Log.d("onSensorChanged", "onSensorChanged: "+acceleration+" "+SHAKE_THRESHOLD);
                            isDialog = false;
                            final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
                            viewDialog.setIcon(android.R.drawable.btn_star_big_on);
                            viewDialog.setTitle("ข้อความ");
                            viewDialog.setMessage("โทรศัพมีการเขย่า");
                            viewDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            isDialog = true;
                                            dialog.dismiss();
                                        }
                                    });
                            viewDialog.show();
                        }

                       /* semsiImageView.startAnimation(animShake);
                        count++;
                        if (count == 5) {
                            check = false;
                            semsiImageView.setVisibility(View.GONE);
                            showPredict();
                        }*/


                    }
                }
            }

        }
    }

    private void showPredict() {
        stickImageView.setVisibility(View.VISIBLE);
        stickImageView.startAnimation(stickAnimation);

        final AlertDialog.Builder viewDialog = new AlertDialog.Builder(this);
        viewDialog.setIcon(android.R.drawable.btn_star_big_on);
        viewDialog.setTitle("ขอความ ้ ");
        viewDialog.setMessage("โทรศพทั ม์ การเขย ี า่ ");
        viewDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        viewDialog.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
