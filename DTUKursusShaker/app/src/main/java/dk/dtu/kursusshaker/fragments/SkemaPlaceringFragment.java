package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashSet;

import dk.dtu.kursusshaker.PreferenceData;
import dk.dtu.kursusshaker.R;

public class SkemaPlaceringFragment extends Fragment implements PreferenceData {
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

    HashSet<String> skemaplaceringer = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_skema_placering, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cbMonM = getView().findViewById(R.id.cb_man_m);
        cbMonE = getView().findViewById(R.id.cb_man_e);
        cbTirM = getView().findViewById(R.id.cb_tir_m);
        cbTirE = getView().findViewById(R.id.cb_tir_e);
        cbTirA = getView().findViewById(R.id.cb_tir_a);
        cbOnsM = getView().findViewById(R.id.cb_ons_m);
        cbOnsE = getView().findViewById(R.id.cb_ons_e);
        cbTorM = getView().findViewById(R.id.cb_tor_m);
        cbTorE = getView().findViewById(R.id.cb_tor_e);
        cbFreM = getView().findViewById(R.id.cb_fre_m);
        cbFreE = getView().findViewById(R.id.cb_fre_e);
    }


    @Override
    public void savePreferenceData() {
        SharedPreferences sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        if ()



        sp.edit().putStringSet("Skemaplacering", );

    }
}
