package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashSet;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;
import dk.dtu.kursusshaker.data.PrimaryViewModel;

public class PrimaryActivity extends AppCompatActivity {

    private static final String TAG = "Debug";
    private int backButtonCounter;
    private static MenuItem item;
    private static NavController navController;
    NavController navigationController = null;
    PrimaryViewModel primaryViewModel;

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;
    HashSet<String> takenCourses;

    //Sigurd, skal slettes senere
    Button resetPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        if (savedInstanceState == null) {
            setupButtomNavigationBar();
            primaryViewModel = ViewModelProviders.of(this).get(PrimaryViewModel.class);
            primaryViewModel.callViewModel();
        }

        sp = getSharedPreferences("Preferences", MODE_PRIVATE);

        takenCourses = (HashSet<String>) sp.getStringSet("Courses",new HashSet<String>());

        resetPrefs = findViewById(R.id.button_reset);
        resetPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("Preferences", MODE_PRIVATE).edit().clear().apply();
            }
        });
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
            /*
            try {
                Toast.makeText(getApplicationContext(), destination.getLabel().toString(), Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "NULL DESTINATION", Toast.LENGTH_SHORT).show();
            }
            */

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Course returnedcourse = (Course) data.getSerializableExtra("returnedCourse");
        // requestCode 1 equals the intent request made from SearchFragment if a search item is
        // clicked within the primaryActivity scope

        sp.edit().remove("Courses").apply();

        if (takenCourses.contains(returnedcourse.getCourseCode())) {
            Toast.makeText(getApplicationContext(), "You already added this course", Toast.LENGTH_SHORT).show();
        } else {
            takenCourses.add(returnedcourse.getCourseCode());
            primaryViewModel.addCourseToBasketArrayList(returnedcourse);
            Toast.makeText(getApplicationContext(), returnedcourse.getDanishTitle() + " tilføjet til kurven!", Toast.LENGTH_SHORT).show();
        }

        sp.edit().putStringSet("Courses", takenCourses).apply();
    }
}
