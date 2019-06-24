package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import dk.dtu.kursusshaker.PreferenceData;
import dk.dtu.kursusshaker.R;

public class SkemaPlaceringFragment extends OnboardingFragment {
    private CheckBox cbMonM;
    private CheckBox cbTirM;
    private CheckBox cbOnsM;
    private CheckBox cbTorM;
    private CheckBox cbFreM;
    private CheckBox cbMonE;
    private CheckBox cbTirE;
    private CheckBox cbOnsE;
    private CheckBox cbTorE;
    private CheckBox cbFreE;
    private CheckBox cbTirA;

    ArrayList<CheckBox> checkBoxes;

    private final int NUM_CHECK_BOX = 11;

    private HashSet<String> skemaplaceringer = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skema_placering, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbMonM = getView().findViewById(R.id.cb_1a);
        cbMonE = getView().findViewById(R.id.cb_2a);
        cbTirM = getView().findViewById(R.id.cb_3a);
        cbTirE = getView().findViewById(R.id.cb_4a);
        cbTirA = getView().findViewById(R.id.cb_aftenmodul);
        cbOnsM = getView().findViewById(R.id.cb_5a);
        cbOnsE = getView().findViewById(R.id.cb_5b);
        cbTorM = getView().findViewById(R.id.cb_2b);
        cbTorE = getView().findViewById(R.id.cb_1b);
        cbFreM = getView().findViewById(R.id.cb_4b);
        cbFreE = getView().findViewById(R.id.cb_3b);

        checkBoxes = new ArrayList<>();

        checkBoxes.add(cbMonM);
        checkBoxes.add(cbMonE);
        checkBoxes.add(cbTirM);
        checkBoxes.add(cbTirE);
        checkBoxes.add(cbTirA);
        checkBoxes.add(cbOnsM);
        checkBoxes.add(cbOnsE);
        checkBoxes.add(cbTorM);
        checkBoxes.add(cbTorE);
        checkBoxes.add(cbFreM);
        checkBoxes.add(cbFreE);
    }


    @Override
    public void savePreferenceData() {
        SharedPreferences sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        for (int i = 0; i < NUM_CHECK_BOX; i++) {
            CheckBox currentBox = checkBoxes.get(i);
            if(currentBox.isChecked()) {
                String string = getResources().getResourceName(currentBox.getId());
                String[] formattedSkema = string.split("_");
                String formattedSkemaPlacering = formattedSkema[1];

                skemaplaceringer.add(formattedSkemaPlacering.toUpperCase());
            }
        }

        sp.edit().putStringSet("Skemaplacering", skemaplaceringer).apply();
    }
}
