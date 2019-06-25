package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.activities.ViewCourseActivity;
import dk.dtu.kursusshaker.data.Course;
import dk.dtu.kursusshaker.data.CoursesAsObject;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;
import dk.dtu.kursusshaker.data.PrimaryViewModel;


public class BasketFragment extends Fragment {
    private static final String TAG = "BasketFragment";

    SharedPreferences sp;
    public SimpleAdapter simpleAdapter;


    private ListView listView;

    private PrimaryViewModel primaryViewModel;

    public BasketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        primaryViewModel = ViewModelProviders.of(getActivity()).get(PrimaryViewModel.class);
        int basketArraySize = primaryViewModel.getSizeOfBasketArrayList();

        // Inflate the layout for this fragment
        final FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_basket, container, false);

        drawListView(frameLayout);

        return frameLayout;
    }

    private void drawListView(FrameLayout frameLayout){
        //Parse hashset to arraylist
        HashSet<String> basketCourses = (HashSet<String>) sp.getStringSet("BasketCourses", new HashSet<String>());
        CoursesAsObject coursesAsObject = new CoursesAsObject(getContext());

        ArrayList<String> coursesAsArrayList = new ArrayList<>(basketCourses);
        ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < coursesAsArrayList.size(); i++) {
            Map<String, Object> listItemMap = new HashMap<String, Object>();
            listItemMap.put("title", coursesAsObject.getCourseFromId(coursesAsArrayList.get(i)).getDanishTitle());
            listItemMap.put("description", coursesAsArrayList.get(i));
            itemDataList.add(listItemMap);
        }

        simpleAdapter = new SimpleAdapter(getActivity(), itemDataList, android.R.layout.simple_list_item_2,
                new String[]{"title", "description"}, new int[]{android.R.id.text1, android.R.id.text2});


        listView = frameLayout.findViewById(R.id.basket_list);
        listView.setAdapter(simpleAdapter);
    }
}
