package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dk.dtu.kursusshaker.R;

public class PrimaryActivity extends AppCompatActivity {

    private int backButtonCounter;
    private static MenuItem item;
    private static NavController navController;
    NavController navigationController = null;
    //BottomNavigationView bottomNavigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        if (savedInstanceState == null) {
            setupButtomNavigationBar();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        setupButtomNavigationBar();
    }

    private void setupButtomNavigationBar() {
        navigationController = Navigation.findNavController(this, R.id.primary_host_fragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        navigationController.setGraph(R.navigation.nav_graph);

        // Connect view and controller and setup listeners
        Navigation.setViewNavController(bottomNavigationView, navigationController); // Link bottomNavigationView together with controller
        NavigationUI.setupWithNavController(bottomNavigationView, navigationController); // Link bottomNavigationUI "visual elements" together with controller
        findViewById(R.id.navigation_dashboard).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_dashboard));
        findViewById(R.id.navigation_search).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_search));
        findViewById(R.id.navigation_basket).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_basket));
        findViewById(R.id.navigation_settings).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.navigation_settings));

        //Assign listeners
        navigationController.addOnDestinationChangedListener(onDestinationChangedListener);

    }

    NavController.OnDestinationChangedListener onDestinationChangedListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

            // Usefull method.. Following can be removed before final release!
            if (destination.getId() == R.id.navigation_dashboard) {
                Toast.makeText(getApplicationContext(), "Changed to Dashboard Fragment", Toast.LENGTH_SHORT).show();
            } else if (destination.getId() == R.id.navigation_search) {
                Toast.makeText(getApplicationContext(), "Changed to Search Fragment", Toast.LENGTH_SHORT).show();
            } else if (destination.getId() == R.id.navigation_basket) {
                Toast.makeText(getApplicationContext(), "Changed to Basket Fragment", Toast.LENGTH_SHORT).show();
            } else if (destination.getId() == R.id.navigation_settings) {
                Toast.makeText(getApplicationContext(), "Changed to Settings Fragment", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // When user press back during mainMenuTabs he will be asked if he wish to hide the app
    @Override
    public void onBackPressed() {
        Toast myToast = Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT);
        if (backButtonCounter >= 1) {
            myToast.cancel();
            backButtonCounter = 0;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            myToast.show();
            backButtonCounter++;
        }
    }

}
