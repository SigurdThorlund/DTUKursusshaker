package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.CourseFilterBuilder;
import dk.dtu.kursusshaker.data.CoursesAsObject;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;
import dk.dtu.kursusshaker.data.PrimaryViewModel;
import dk.dtu.kursusshaker.fragments.DashboardFragment;
import dk.dtu.kursusshaker.fragments.SearchFragment;

public class PrimaryActivity extends AppCompatActivity {

    private static final String TAG = "Debug";
    private int backButtonCounter;
    private static MenuItem item;
    private static NavController navController;
    NavController navigationController = null;
    PrimaryViewModel primaryViewModel;

    SharedPreferences sp;
    HashSet<String> takenCourses;
    HashSet<String> basketCourses;

    //Sigurd, skal slettes senere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        if (savedInstanceState == null) {
            setupButtomNavigationBar();
            primaryViewModel = ViewModelProviders.of(this).get(PrimaryViewModel.class);
            primaryViewModel.callViewModel();
            initFilteredCourses();
        }

        sp = getSharedPreferences("Preferences", MODE_PRIVATE);

        takenCourses = (HashSet<String>) sp.getStringSet("Courses",new HashSet<String>());
        basketCourses = (HashSet<String>) sp.getStringSet("BasketCourses",new HashSet<String>());
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

    private void initFilteredCourses() {
        CoursesAsObject coursesAsObject = new CoursesAsObject(getApplicationContext());

        String season = "";
        String[] scheduleFilter = {};
        String[] teachingLanguages = {};
        String[] locations = {};
        String type = "DTU_DIPLOM";
        String[] departments = {};
        String[] ects = {};

        sp = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        takenCourses = (HashSet<String>) sp.getStringSet("Courses", new HashSet<String>());

        String[] completed = takenCourses.toArray(new String[takenCourses.size()]);

        primaryViewModel.setCourseFilterBuilder(new CourseFilterBuilder(coursesAsObject, season,
                scheduleFilter, completed, teachingLanguages, locations, type, departments, ects));
    }

    NavController.OnDestinationChangedListener onDestinationChangedListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

            if (destination.getId() != R.id.navigation_dashboard) {
            }

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
        Toast myToast = Toast.makeText(this, "Tryk tilbage igen for at afslutte!", Toast.LENGTH_SHORT);
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


        // requestCode 1 equals the intent request made from SearchFragment if a search item is
        // clicked within the primaryActivity scope
        basketCourses = (HashSet<String>) sp.getStringSet("BasketCourses",new HashSet<String>());

        sp.edit().remove("BasketCourses").apply();

        if (resultCode == 1) {
            Course returnedcourse = (Course) data.getSerializableExtra("returnedCourse");

            if (basketCourses.contains(returnedcourse.getCourseCode())) {
                Toast toast = Toast.makeText(getApplicationContext(), "Du har allerede tilføjet dette kursus", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            } else {
                basketCourses.add(returnedcourse.getCourseCode());
                Toast toast = Toast.makeText(getApplicationContext(), returnedcourse.getDanishTitle() + " tilføjet til kurven!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                toast.show();
            }
        }

        sp.edit().putStringSet("BasketCourses", basketCourses).apply();

    }
}
