package dk.dtu.kursusshaker;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import dk.dtu.kursusshaker.activities.PrimaryActivity;

// Main entry-point for the app. Mby this is a good place for Firebase initialization????
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Creating intent and allowing data to be handled prior to the application starts
        Intent startPrimaryActivityIntent = new Intent(getApplicationContext(), PrimaryActivity.class);
        startActivity(startPrimaryActivityIntent);
        finish();
    }

}
