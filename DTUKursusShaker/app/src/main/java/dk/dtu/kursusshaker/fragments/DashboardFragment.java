package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashSet;

import dk.dtu.kursusshaker.OnShakeListener;
import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.ShakeListener;
import dk.dtu.kursusshaker.data.CourseFilterBuilder;
import dk.dtu.kursusshaker.data.PrimaryViewModel;

public class DashboardFragment extends Fragment {

    PrimaryViewModel primaryViewModel;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private ShakeListener sensorListener;
    private static final String TAG = "tag";

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        //Del af koden fra http://jasonmcreynolds.com/?p=388
        ///////////////////////////
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new ShakeListener(new OnShakeListener() {
            @Override
            public void onShake() {
        //////////////////////////
                CourseFilterBuilder courseFilterBuilder = primaryViewModel.getCourseFilterBuilder();
                primaryViewModel.setRecommendedCourse(courseFilterBuilder.getRandomCourse());
                Navigation.findNavController(getActivity(), R.id.primary_host_fragment).navigate(R.id.recommendationsFragment);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        primaryViewModel = ViewModelProviders.of(getActivity()).get(PrimaryViewModel.class);

        // Inflate the layout for this fragment
        final ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.fragment_dashboard, container, false);

        ImageButton shakeItButton = (ImageButton) constraintLayout.getViewById(R.id.shakeViewButton);
        shakeItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseFilterBuilder courseFilterBuilder = primaryViewModel.getCourseFilterBuilder();
                primaryViewModel.setRecommendedCourse(courseFilterBuilder.getRandomCourse());
                Navigation.findNavController(constraintLayout).navigate(R.id.recommendationsFragment);
            }
        });
        return constraintLayout;
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop() {
        sensorManager.unregisterListener(sensorListener);
        super.onStop();
    }
}
