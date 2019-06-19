package dk.dtu.kursusshaker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import dk.dtu.kursusshaker.activities.PrimaryActivity;

// Main entry-point for the app. Mby this is a good place for Firebase initialization????
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "nej"; //todo change

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Creating intent and allowing data to be handled prior to the application starts
        Intent startPrimaryActivityIntent = new Intent(getApplicationContext(), PrimaryActivity.class);
        startActivity(startPrimaryActivityIntent);
        finish();
    }


    //Create notifications channel
    private void createNotificationChannel() {
        Log.i(TAG,"Entered createNotificationChannel()");
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("DTU_K_shaker", "Kursusshaker_Notifications", importance); //Need for better channel name+id
            channel.setDescription("Notifications from DTU Kursusshaker");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
