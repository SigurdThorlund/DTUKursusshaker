package dk.dtu.kursusshaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import dk.dtu.kursusshaker.MainActivity;
import dk.dtu.kursusshaker.R;

/**
 * Activity for splashscreen. Will appear shortly and start the mainactivity
 * The layout is set in the android manifest as, splash_screen.xml.
 *
 * Sigurd Thorlund s184189
 *
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mainIntent = new Intent(SplashActivity.this, OnboardingActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
