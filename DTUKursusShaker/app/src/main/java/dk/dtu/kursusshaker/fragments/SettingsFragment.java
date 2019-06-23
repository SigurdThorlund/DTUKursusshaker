package dk.dtu.kursusshaker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dk.dtu.kursusshaker.R;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;


import dk.dtu.kursusshaker.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.d("KEY", rootKey + "KEY");
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}
