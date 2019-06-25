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


public class SearchFragment extends Fragment {

    private static final String WHAT_FRAGMENT_HOST = "WHAT_FRAGMENT_HOST";


    public View view;
    public SimpleAdapter simpleAdapter;

    public ListView listView;

    OnBoardingViewModel onBoardingViewModel;
    PrimaryViewModel primaryViewModel;

    CoursesAsObject coursesAsObject;

    SharedPreferences sp;
    HashSet<String> takenCourses;
    HashSet<String> basketCourses;
    HashSet<String> schedulePlacements;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void insertCoursesInListView() throws IOException { //TODO skal laves til MVC
        coursesAsObject = new CoursesAsObject(getContext());

        ArrayList<Course> courseArray;

        if (onBoardingViewModel.getOnBoardingInProgress()) {
            courseArray = new ArrayList<>(Arrays.asList(coursesAsObject.getCourseArray()));
        } else {
            String[] teachingLanguages = {};
            String[] locations = {};
            String[] departments = {};
            String[] ects = {};

            sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            takenCourses = (HashSet<String>) sp.getStringSet("Courses", new HashSet<String>());

            schedulePlacements = (HashSet<String>) sp.getStringSet("Skemaplacering", new HashSet<String>());

            String type = sp.getString("Kursustype", "DTU_BSC");
            String season = sp.getString("Semestertid", "E");

            String[] completed = takenCourses.toArray(new String[takenCourses.size()]);
            String[] scheduleFilter = schedulePlacements.toArray(new String[schedulePlacements.size()]);

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
}
