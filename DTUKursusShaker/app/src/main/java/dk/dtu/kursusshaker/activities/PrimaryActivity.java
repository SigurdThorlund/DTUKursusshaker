package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;

import java.util.List;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.fragments.BasketFragment;
import dk.dtu.kursusshaker.fragments.DashboardFragment;
import dk.dtu.kursusshaker.fragments.SearchFragment;
import dk.dtu.kursusshaker.fragments.SettingsFragment;

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
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        // This links bottomNavigationView together with navigationController...
        // But documentation is not clear on how to use it. Examples only exists for Kotlin..
        NavigationUI.setupWithNavController(bottomNavigationView, navigationController);

        //Assign listeners
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navigationController.addOnDestinationChangedListener(onDestinationChangedListener);

    }

    NavController.OnDestinationChangedListener onDestinationChangedListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

            // Usefull method.. Following can be removed before final release!
            if (destination.getId() == R.id.dashboardFragment) {
                Toast.makeText(getApplicationContext(), "Changed to Dashboard Fragment", Toast.LENGTH_SHORT).show();
            } else if (destination.getId() == R.id.searchFragment) {
                Toast.makeText(getApplicationContext(), "Changed to Search Fragment", Toast.LENGTH_SHORT).show();
            } else if (destination.getId() == R.id.basketFragment) {
                Toast.makeText(getApplicationContext(), "Changed to Basket Fragment", Toast.LENGTH_SHORT).show();
            } else if (destination.getId() == R.id.settingsFragment) {
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

    AppBarConfiguration.OnNavigateUpListener onNavigateUpListener = new AppBarConfiguration.OnNavigateUpListener() {
        @Override
        public boolean onNavigateUp() {
            return false;
        }
    };

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.navigation_dashboard:
                            navigationController.navigate(R.id.dashboardFragment);
                            return true;
                        case R.id.navigation_search:
                            navigationController.navigate(R.id.searchFragment);
                            return true;
                        case R.id.navigation_basket:
                            navigationController.navigate(R.id.basketFragment);
                            return true;
                        case R.id.navigation_settings:
                            navigationController.navigate(R.id.settingsFragment);
                            return true;
                    }
                    return true;
                }
            };
}
