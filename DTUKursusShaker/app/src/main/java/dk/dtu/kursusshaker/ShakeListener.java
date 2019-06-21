package dk.dtu.kursusshaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeListener implements SensorEventListener {
    private static final int MIN_SHAKE_STENGHT = 5; //Minimum acceleration need to register a shake
    private static final int MIN_MOVEMENTS = 2; //The minimum amount of movements needed to register a shake
    private static final int MAX_SHAKE_TIME = 500; //The maximum time a shake needs to last inorder to be registered
    private float[] gravity = { 0.0f, 0.0f, 0.0f };
    private float[] linearAcceleration = { 0.0f, 0.0f, 0.0f };
    private OnShakeListener onShakeListener;
    long startTime = 0;
    int moveCount = 0;
    public ShakeListener(OnShakeListener shakeListener) {
        onShakeListener = shakeListener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        setCurrentAcceleration(event);
        float maxLinearAcceleration = getMaxCurrentLinearAcceleration();
        if (maxLinearAcceleration > MIN_SHAKE_STENGHT) {
            long now = System.currentTimeMillis();
            if (startTime == 0) {
                startTime = now;
            }
            long elapsedTime = now - startTime;
            if (elapsedTime > MAX_SHAKE_TIME) {
                resetShakeDetection();
            }
            else {
                moveCount++;

                if (moveCount > MIN_MOVEMENTS) {
                    onShakeListener.onShake();
                    resetShakeDetection();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Unused interface method, needs to be kept though
        //since interface demands implementation
    }

    private void setCurrentAcceleration(SensorEvent event) {
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
        // Start by setting the value to the x value
        float maxLinearAcceleration = linearAcceleration[0];
        // Check if the y value is greater
        if (linearAcceleration[1] > maxLinearAcceleration) {
            maxLinearAcceleration = linearAcceleration[1];
        }
        // Check if the z value is greater
        if (linearAcceleration[2] > maxLinearAcceleration) {
            maxLinearAcceleration = linearAcceleration[2];
        }
        // Return the greatest value
        return maxLinearAcceleration;
    }

    private void resetShakeDetection() {
        startTime = 0;
        moveCount = 0;
    }
}
