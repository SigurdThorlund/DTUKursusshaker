package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom FragmentAdapter for logic in the ViewPager.
 *
 * Sigurd Thorlund s184189
 *
 */

public class OnboardingFragmentAdapter extends FragmentPagerAdapter {

    private final int NUM_ONBOARDING_PAGES = 2;

    private List<Fragment> fragmentsList = new ArrayList<>();

    public OnboardingFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return NUM_ONBOARDING_PAGES;
    }

    public void addItem(Fragment newFragment) {
        fragmentsList.add(newFragment);
    }

    public List<Fragment> getFragmentsList() {
        return fragmentsList;
    }
}
