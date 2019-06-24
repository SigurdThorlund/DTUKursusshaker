package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.dtu.kursusshaker.MainActivity;
import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.activities.OnboardingActivity;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;

/**
 * Last fragment of the onboarding
 *
 */

public class GetStartedFragment extends OnboardingFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_started, container, false);
    }

    /**
     * When onboarding is complete, onboarded will be set, so that it will not be launched again
     */
    @Override
    public void savePreferenceData() {
        SharedPreferences sp = getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        sp.edit().putBoolean("Onboarded",true).apply();
    }
}
