package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.PrimaryViewModel;

/**
 * Recommendation for course which shows when the phone is shaken
 *
 */

public class RecommendationsFragment extends Fragment {

    private PrimaryViewModel primaryViewModel;
    HashSet<String> basketCourses;
    SharedPreferences sp;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecommendationsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        basketCourses = (HashSet<String>) sp.getStringSet("BasketCourses", new HashSet<String>());

        primaryViewModel = ViewModelProviders.of(getActivity()).get(PrimaryViewModel.class);
        final Course recommendedCourse = primaryViewModel.getRecommendedCourse();

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        TextView recommendedCourseTitleView = view.findViewById(R.id.recommendedCourseTitle);
        recommendedCourseTitleView.setText(recommendedCourse.getDanishTitle());

        TextView recommendedCourseID = view.findViewById(R.id.recommendedCourseNumber);
        recommendedCourseID.setText(recommendedCourse.getCourseCode());

        TextView recommendedCourseDescription = view.findViewById(R.id.recommendedCourseDescription);
        recommendedCourseDescription.setMovementMethod(new ScrollingMovementMethod());
        recommendedCourseDescription.setText(recommendedCourse.getDanishContents());

        // Also add the button as well as an listener
        Button addToBasketButton = view.findViewById(R.id.recommendedCourseAddToBasketButton);
        addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().remove("BasketCourses").apply();
                if (basketCourses.contains(recommendedCourse.getCourseCode())) {
                    Toast toast = Toast.makeText(getContext(), "Du har allerede tilføjet dette kursus", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                } else {
                    basketCourses.add(recommendedCourse.getCourseCode());
                    Toast toast = Toast.makeText(getContext(), recommendedCourse.getDanishTitle() + " tilføjet til kurven!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                    Navigation.findNavController(v).navigate(R.id.navigation_dashboard);
                }
                sp.edit().putStringSet("BasketCourses", basketCourses).apply();
            }
        });

        return view;
    }
}
