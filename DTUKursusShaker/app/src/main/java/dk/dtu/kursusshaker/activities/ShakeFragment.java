package dk.dtu.kursusshaker.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import dk.dtu.kursusshaker.OnShakeListener;
import dk.dtu.kursusshaker.ShakeListener;

public class ShakeFragment extends Fragment {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener sensorListener;
    private static final String TAG = "tag";

    TextView textview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new ShakeListener(new OnShakeListener() {
            @Override
            public void onShake() {
                Toast.makeText(getActivity().getApplicationContext(),"Shake detected!",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }
}