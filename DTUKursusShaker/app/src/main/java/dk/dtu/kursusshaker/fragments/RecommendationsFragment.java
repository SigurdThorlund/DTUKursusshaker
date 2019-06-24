package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.HashSet;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.PrimaryViewModel;
import dk.dtu.kursusshaker.fragments.dummy.DummyContent.DummyItem;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecommendationsFragment extends Fragment {

    private PrimaryViewModel primaryViewModel;

    private OnListFragmentInteractionListener mListener;

    HashSet<String> takenCourses;
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
        takenCourses = (HashSet<String>) sp.getStringSet("Courses", new HashSet<String>());

        primaryViewModel = ViewModelProviders.of(getActivity()).get(PrimaryViewModel.class);
        final Course recommendedCourse = primaryViewModel.getRecommendedCourse();

        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);

        TextView recommendedCourseTitleView = view.findViewById(R.id.recommendedCourseTitle);
        recommendedCourseTitleView.setText(recommendedCourse.getDanishTitle());

        TextView recommendedCourseID = view.findViewById(R.id.recommendedCourseNumber);
        recommendedCourseID.setText(recommendedCourse.getCourseCode());

        TextView recommendedCourseDescription = view.findViewById(R.id.recommendedCourseDescription);
        recommendedCourseDescription.setText(recommendedCourse.getDanishContents());

        // Also add the button as well as an listener
        Button addToBasketButton = view.findViewById(R.id.recommendedCourseAddToBasketButton);
        addToBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (takenCourses.contains(recommendedCourse.getCourseCode())) {
                    Toast toast = Toast.makeText(getContext(), "Du har allerede tilføjet dette kursus", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                } else {
                    takenCourses.add(recommendedCourse.getCourseCode());
                    Toast toast = Toast.makeText(getContext(), recommendedCourse.getDanishTitle() + " tilføjet til kurven!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL,0,0);
                    toast.show();
                    Navigation.findNavController(v).navigate(R.id.navigation_dashboard);
                }
                sp.edit().putStringSet("Courses", takenCourses).apply();
            }
        });

        return view;
    }


    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
