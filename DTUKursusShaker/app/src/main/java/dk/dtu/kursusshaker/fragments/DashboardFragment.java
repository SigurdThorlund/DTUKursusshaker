package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.activities.PrimaryActivity;
import dk.dtu.kursusshaker.activities.ShakeFragment;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.CoursesAsObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SensorManager mSensorManager;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        ShakeFragment shakeFrag = new ShakeFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.navigation_dashboard, shakeFrag, "Shake")
                .addToBackStack(null)
                .commit();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void insertCoursesInListView() throws IOException { //TODO skal laves til MVC
        ArrayList<String> excludedCourses = new ArrayList<String>();

        // Exclude these three courses just for fun
        // TODO: This is where we want to implement actual filtering stuff
        excludedCourses.add("01005");
        excludedCourses.add("01003");
        excludedCourses.add("01006");

        CoursesAsObject coursesAsObject = new CoursesAsObject(getContext());

        Course[] course = coursesAsObject.getCourseArray(excludedCourses);

        String[] courseNames = new String[course.length];
        String[] courseIds = new String[course.length];
        // String[] courseNames = {"Matematik 1", "Matematik 2", "Statistik", "Software", "Android"};
        // String[] courseIds = {"01005", "01839", "32892", "09732", "56782"};
        for (int i = 0; i < course.length; i++) {
            courseNames[i] = course[i].getDanishTitle();
            courseIds[i] = course[i].getCourseCode();
        }

        ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();

        int titleLen = courseNames.length;
        for (int i = 0; i < titleLen; i++) {
            Map<String, Object> listItemMap = new HashMap<String, Object>();
            listItemMap.put("title", courseNames[i]);
            listItemMap.put("description", courseIds[i]);
            itemDataList.add(listItemMap);
        }

        simpleAdapter = new SimpleAdapter(getActivity(), itemDataList, android.R.layout.simple_list_item_2,
                new String[]{"title", "description"}, new int[]{android.R.id.text1, android.R.id.text2});

        ListView listView = (ListView) view.findViewById(R.id.list_item_all_courses);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                //     Toast.makeText(this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        TextInputEditText courseFilter = (TextInputEditText) view.findViewById(R.id.search_all_courses_filter);

        courseFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.fragment_dashboard, container, false);

        ImageButton shakeItButton = (ImageButton) constraintLayout.getViewById(R.id.shakeViewButton);
        shakeItButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(constraintLayout).navigate(R.id.recommendationsFragment);
            }
        });


        return constraintLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);

        // Not sure how to use this piece of code.. But it is the prefered way to glue the connection together
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
