package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.controller.BasketRecyclerViewAdapter;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;
import dk.dtu.kursusshaker.data.PrimaryViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class BasketFragment extends Fragment {
    private static final String TAG = "BasketFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    SharedPreferences sp;


    private ListView listView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private OnBoardingViewModel onBoardingViewModel;
    private PrimaryViewModel primaryViewModel;

    public BasketFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasketFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasketFragment newInstance(String param1, String param2) {
        BasketFragment fragment = new BasketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        primaryViewModel = ViewModelProviders.of(getActivity()).get(PrimaryViewModel.class);
        int basketArraySize = primaryViewModel.getSizeOfBasketArrayList();

        // Inflate the layout for this fragment
        final FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.fragment_basket, container, false);

        //Parse hashset to arraylist
        HashSet<String> courses = (HashSet<String>) sp.getStringSet("Courses", new HashSet<String>());

        ArrayList<String> coursesAsArrayList = new ArrayList<>(courses);

        ArrayList<Map<String, Object>> itemDataList = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < coursesAsArrayList.size(); i++) {
            Map<String, Object> listItemMap = new HashMap<String, Object>();
            listItemMap.put("title", coursesAsArrayList.get(i));
            listItemMap.put("description", coursesAsArrayList.get(i));
            itemDataList.add(listItemMap);

        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(), itemDataList, android.R.layout.simple_list_item_2,
                new String[]{"title", "description"}, new int[]{android.R.id.text1, android.R.id.text2});

        listView = frameLayout.findViewById(R.id.basket_list);
        listView.setAdapter(simpleAdapter);


        return frameLayout;
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
