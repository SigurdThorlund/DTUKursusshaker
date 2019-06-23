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
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.dtu.kursusshaker.activities.PrimaryActivity;

// Main entry-point for the app. Mby this is a good place for Firebase initialization????
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "nej"; //todo change

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();

        // Creating intent and allowing data to be handled prior to the application starts
        Intent startPrimaryActivityIntent = new Intent(getApplicationContext(), PrimaryActivity.class);
        startActivity(startPrimaryActivityIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "DTU_K_shaker")
                .setSmallIcon(R.drawable.ic_home_black_24dp) //Need new icon, current one is a placeholder
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Calendar calendar = Calendar.getInstance();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int thisMonth = calendar.get(Calendar.MONTH);
        if(thisMonth == 11) {
            builder.setContentTitle("DTU Kursusshaker").setContentText("Husk tilmeldning til 3-ugers kursus i januar!");
            notificationManager.notify(0,builder.build());
        }
        else if(thisMonth == 4) {
            builder.setContentTitle("DTU Kursusshaker").setContentText("Husk tilmeldning til 3-ugers sommerkurser!");
            notificationManager.notify(1,builder.build());
        }

// notificationId is a unique int for each notification that you must define
        builder.setContentTitle("DTU Kursusshaker")
                .setContentText("App has launched");
        notificationManager.notify(2, builder.build());
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
