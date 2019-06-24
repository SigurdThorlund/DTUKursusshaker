package dk.dtu.kursusshaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeListener implements SensorEventListener {
    private static final int MIN_SHAKE_STENGHT = 5; //Minimum acceleration need to register a shake
    private static final int MIN_MOVEMENTS = 2; //The minimum amount of movements needed to register a shake
    private static final int MAX_SHAKE_TIME = 500; //The maximum time a shake needs to last inorder to be registered
    private float[] gravity = { 0.0f, 0.0f, 0.0f };
    private float[] linearAcceleration = { 0.0f, 0.0f, 0.0f };
    private OnShakeListener onShakeListener;
    long startTime = 0;
    int moveCount = 0;

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.3F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;


    private long mShakeTimestamp;
    private int mShakeCount;


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

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                onShakeListener.onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Unused interface method, needs to be kept though
        //since interface demands implementation
    }

    /*private void setCurrentAcceleration(SensorEvent event) {
        final float alpha = 0.8f;

        // Gravity components of x, y, and z acceleration
        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        // Linear acceleration along the x, y, and z axes (gravity effects removed)
        linearAcceleration[0] = event.values[0] - gravity[0];
        linearAcceleration[1] = event.values[1] - gravity[1];
        linearAcceleration[2] = event.values[2] - gravity[2];
    }

    private float getMaxCurrentLinearAcceleration() {
        float maxLinearAcceleration = linearAcceleration[0];
        if (linearAcceleration[1] > maxLinearAcceleration) {
            maxLinearAcceleration = linearAcceleration[1];
        }
        if (linearAcceleration[2] > maxLinearAcceleration) {
            maxLinearAcceleration = linearAcceleration[2];
        }
        return maxLinearAcceleration;
    }*/

    private void resetShakeDetection() {
        startTime = 0;
        moveCount = 0;
    }
}
