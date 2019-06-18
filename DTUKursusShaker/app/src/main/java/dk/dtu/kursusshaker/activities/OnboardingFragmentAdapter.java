package dk.dtu.kursusshaker.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class OnboardingFragmentAdapter extends FragmentPagerAdapter {

    private final int NUM_ONBOARDING_PAGES = 4;

    public OnboardingFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new OnboardingFragment();
        Bundle args = new Bundle();
        args.putInt(OnboardingFragment.ARG_OBJECT,position+1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ONBOARDING_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT" + (position + 1);
    }
}
