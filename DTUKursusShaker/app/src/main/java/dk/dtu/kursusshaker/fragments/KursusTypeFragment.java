package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import dk.dtu.kursusshaker.PreferenceData;
import dk.dtu.kursusshaker.R;

public class KursusTypeFragment extends OnboardingFragment {
    private RadioButton checkedButton;
    private RadioGroup group;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kursus_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        group = getView().findViewById(R.id.kursus_group);
    }

    @Override
    public void savePreferenceData() {
        checkedButton = getView().findViewById(group.getCheckedRadioButtonId());
        SharedPreferences.Editor sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE).edit();
        sp.putString("Kursustype",(String) checkedButton.getText());
        sp.apply();
    }
}
