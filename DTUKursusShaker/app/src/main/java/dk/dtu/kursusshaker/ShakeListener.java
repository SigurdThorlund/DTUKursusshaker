package dk.dtu.kursusshaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListener implements SensorEventListener {
    private OnShakeListener onShakeListener;
    long startTime = 0;
    int moveCount = 0;

    private static final float ACCELERATION_THRESHHOLD = 2.2F;
    private static final int SHAKE_MAX_DURATION = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
    private long timeAtStart;
    private int count;


    public ShakeListener(OnShakeListener shakeListener) {
        onShakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (onShakeListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float gForceEffectX = x / SensorManager.GRAVITY_EARTH;
            float gForceEffectY = y / SensorManager.GRAVITY_EARTH;
            float gForceEffectZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float) Math.sqrt(gForceEffectX * gForceEffectX + gForceEffectY * gForceEffectY + gForceEffectZ * gForceEffectZ);

            if (gForce > ACCELERATION_THRESHHOLD) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (startTime + SHAKE_MAX_DURATION > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (timeAtStart + SHAKE_COUNT_RESET_TIME_MS < now) {
                    count = 0;
                }

                startTime = now;
                count++;

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
