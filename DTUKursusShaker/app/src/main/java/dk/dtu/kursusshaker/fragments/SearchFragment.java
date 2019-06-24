package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.activities.ViewCourseActivity;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.CourseFilterBuilder;
import dk.dtu.kursusshaker.data.CoursesAsObject;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;
import dk.dtu.kursusshaker.data.PrimaryViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private static final String WHAT_FRAGMENT_HOST = "WHAT_FRAGMENT_HOST";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String SOURCE = "param1";
    private static final String ACTION = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public View view;
    public SimpleAdapter simpleAdapter;

    private OnFragmentInteractionListener mListener;
    public ListView listView;

    OnBoardingViewModel onBoardingViewModel;
    PrimaryViewModel primaryViewModel;

    CoursesAsObject coursesAsObject;


    SharedPreferences sp;
    HashSet<String> takenCourses;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(SOURCE, param1);
        args.putString(ACTION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(SOURCE);
            mParam2 = getArguments().getString(ACTION);
        }
    }

    private void insertCoursesInListView() throws IOException { //TODO skal laves til MVC
        coursesAsObject = new CoursesAsObject(getContext());

        ArrayList<Course> courseArray;

        if (onBoardingViewModel.getOnBoardingInProgress()) {
            courseArray = new ArrayList<>(Arrays.asList(coursesAsObject.getCourseArray()));
        } else {
            String season = "";
            String[] scheduleFilter = {};
            String[] teachingLanguages = {};
            String[] locations = {};
            String type = "DTU_DIPLOM";
            String[] departments = {};
            String[] ects = {};

            sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            takenCourses = (HashSet<String>) sp.getStringSet("Courses", new HashSet<String>());

            String[] completed = takenCourses.toArray(new String[takenCourses.size()]);

            primaryViewModel.setCourseFilterBuilder(new CourseFilterBuilder(coursesAsObject, season,
                    scheduleFilter, completed, teachingLanguages, locations, type, departments, ects));

            courseArray = primaryViewModel.getCourseFilterBuilder().filterAllCourses();
        }

        String[] courseNames = new String[courseArray.size()];
        String[] courseIds = new String[courseArray.size()];


        // Reads course names and IDs into arrays
        int j = 0;
        for (Course currentCourse : courseArray) {
            courseNames[j] = currentCourse.getDanishTitle();
            courseIds[j] = currentCourse.getCourseCode();
            j++;
        }

        // Shows course names and IDs in the listview
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

        listView = (ListView) view.findViewById(R.id.list_item_all_courses);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(onItemClickListener);

        TextInputEditText courseFilter = (TextInputEditText) view.findViewById(R.id.search_all_courses_filter);

        courseFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                simpleAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    // Listener that chooses the behavior when user clicks an Item in the list
    public AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
            HashMap clickItemObj = (HashMap) adapterView.getAdapter().getItem(index);
            String selectedCourseCode = (String) clickItemObj.get("description");
            Course intentCourse = coursesAsObject.getCourseFromId(selectedCourseCode);

            // Let the listView behave differently on userInput based on application state
            if (onBoardingViewModel.getOnBoardingInProgress()) {
                if (onBoardingViewModel.addFinishedCourseToHashSet(intentCourse.getCourseCode())) {
                    onBoardingViewModel.callViewModel();
                    Toast toast = Toast.makeText(getContext(), "Course: " + intentCourse.getCourseCode() + " added", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getContext(), "Course already added!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }

            } else {
                // Following code is run when in primaryActivity aka. when onBoarding is done
                // the intent goes from PrimaryActivity to ViewCourseActivity
                // We make an intent with result in case the user adds the course to his/her basket
                // and then the result course is added to the PrimaryViewModel so the basketFragment can interact with it!

                Intent intent = new Intent(getContext(), ViewCourseActivity.class);
                intent.putExtra("selectedCourse", intentCourse);
                startActivityForResult(intent, 1);
            }
        }
    };


    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(WHAT_FRAGMENT_HOST, "SearchFragment is attached to: " + getActivity());

        // Link SearchFragment to the OnBoardingViewModel
        onBoardingViewModel = ViewModelProviders.of(this.getActivity()).get(OnBoardingViewModel.class);

        // Link SearchFragment to the PrimaryViewModel
        primaryViewModel = ViewModelProviders.of(getActivity()).get(PrimaryViewModel.class);

        view = inflater.inflate(R.layout.fragment_search, container, false);
        try {
            this.insertCoursesInListView();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
        //Log.e(WHAT_FRAGMENT_HOST, "SearchFragment is attached to: " + getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void sayHello() {
        Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
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
