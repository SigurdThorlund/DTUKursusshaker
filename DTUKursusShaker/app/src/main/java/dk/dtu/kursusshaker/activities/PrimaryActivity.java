package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.fragments.BasketFragment;
import dk.dtu.kursusshaker.fragments.DashboardFragment;
import dk.dtu.kursusshaker.fragments.SearchFragment;
import dk.dtu.kursusshaker.fragments.SettingsFragment;

public class PrimaryActivity extends AppCompatActivity {

    private NavController navigationController = null;

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
        BottomNavigationView navigationView = findViewById(R.id.nav_view);
        navigationController = Navigation.findNavController(this, R.id.primary_host_fragment);
        NavigationUI.setupWithNavController(navigationView, navigationController);

        //navigationController.addOnDestinationChangedListener(onDestinationChangedListener);

        //Deprecated most likely...
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    NavController.OnDestinationChangedListener onDestinationChangedListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

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
