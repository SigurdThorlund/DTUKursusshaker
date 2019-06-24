package dk.dtu.kursusshaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


//Den klasse og alle andre kodedele for implementationen af Shake-Detection
//er kode fra http://jasonmcreynolds.com/?p=388 som er blevet tilpasset til vores behov
public class ShakeListener implements SensorEventListener {
    private OnShakeListener onShakeListener;;
    private static final float ACCELERATION_THRESHHOLD = 2.2F;
    private static final int SHAKE_MAX_DURATION = 500;
    private long startTime;

    public ShakeListener(OnShakeListener shakeListener) {
        onShakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (onShakeListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gX = x / SensorManager.GRAVITY_EARTH;
            float gY = y / SensorManager.GRAVITY_EARTH;
            float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if (gForce > ACCELERATION_THRESHHOLD) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (startTime + SHAKE_MAX_DURATION > now) {
                    return;
                }
                startTime = now;

                onShakeListener.onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Unused interface method, needs to be kept though
        //since interface demands implementation
    }
}
