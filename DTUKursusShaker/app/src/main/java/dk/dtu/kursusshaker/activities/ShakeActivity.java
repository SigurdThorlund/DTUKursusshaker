package dk.dtu.kursusshaker.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import dk.dtu.kursusshaker.OnShakeListener;
import dk.dtu.kursusshaker.ShakeListener;

import static android.widget.Toast.makeText;

public class ShakeActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener sensorListener;
    private static final String TAG = "tag";

    TextView textview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new ShakeListener(new OnShakeListener() {
            @Override
            public void onShake() {
                Toast.makeText(getApplicationContext(),"Shake detected!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }
}