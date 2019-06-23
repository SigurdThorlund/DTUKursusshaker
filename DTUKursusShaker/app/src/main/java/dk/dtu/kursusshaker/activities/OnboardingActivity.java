package dk.dtu.kursusshaker.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import dk.dtu.kursusshaker.MainActivity;
import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.fragments.GetStartedFragment;
import dk.dtu.kursusshaker.controller.OnboardingFragmentAdapter;
import dk.dtu.kursusshaker.data.OnBoardingViewModel;
import dk.dtu.kursusshaker.fragments.KursusTypeFragment;
import dk.dtu.kursusshaker.fragments.OnboardingFragment;
import dk.dtu.kursusshaker.fragments.SkemaPlaceringFragment;
import dk.dtu.kursusshaker.fragments.TagetKursusFragment;

import static androidx.lifecycle.ViewModelProviders.of;

/**
 * Activity that hosts the onboarding fragments
 *
 * Sigurd Thorlund s184189
 */

public class OnboardingActivity extends AppCompatActivity {
    OnboardingFragmentAdapter fragmentAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    Context context;

    Button buttonBack;
    Button buttonNext;

    private View.OnClickListener nextDefaultListener;
    private View.OnClickListener nextLastViewListener;

    KursusTypeFragment kursusTypeFragment;
    SkemaPlaceringFragment skemaPlaceringFragment;
    TagetKursusFragment tagetKursusFragment;
    GetStartedFragment getStartedFragment;

    OnboardingFragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //If onbaording has been completed launch the main activity
        if (restorePreferences()) {
            launchMainActivity();
        }

        setContentView(R.layout.activity_intro);

        context = getApplicationContext();

        //Connect adapter, tablayout and viewpager.
        fragmentAdapter = new OnboardingFragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.fragment_viewpage);
        viewPager.setAdapter(fragmentAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        final OnBoardingViewModel onBoardingViewModel = ViewModelProviders.of(this).get(OnBoardingViewModel.class);
        onBoardingViewModel.setOnboardingInProgress(true);

        //Add the fragments to the layout
        setupOnboarding();

        //Back and Next buttons functionality
        buttonNext = findViewById(R.id.button_next);

        nextDefaultListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position < fragmentAdapter.getFragmentsList().size()) {
                    position++;
                    viewPager.setCurrentItem(position);
                }
            }
        };

        buttonNext.setOnClickListener(nextDefaultListener);

        //Listener for when the fragment is on the last view.
        nextLastViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBoardingViewModel.setOnboardingInProgress(false);
                getStartedFragment.savePreferenceData();
                launchMainActivity();
            }
        };

        buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position > 0) {
                    position--;
                    viewPager.setCurrentItem(position);
                }
            }
        });
        buttonBack.setVisibility(View.INVISIBLE);

        //Viewpager updates view while user is onboarding
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateButtonView(position);
                if (position != 0) {
                    currentFragment = (OnboardingFragment) fragmentAdapter.getItem(position-1);
                    currentFragment.savePreferenceData();
                } else {
                    currentFragment = (OnboardingFragment) fragmentAdapter.getItem(position);
                    currentFragment.savePreferenceData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Sets the text and visibility of buttons when the view changes.
     *
     * @param position
     */

    private void updateButtonView(int position) {
        if (position == fragmentAdapter.getFragmentsList().size()-1) {
            buttonNext.setOnClickListener(nextLastViewListener);
            buttonNext.setText(R.string.button_done);
        } else if (position == 0) {
            buttonBack.setVisibility(View.INVISIBLE);
        } else {
            buttonNext.setOnClickListener(nextDefaultListener);
            buttonNext.setText(R.string.button_next);
            buttonBack.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Adds fragments to the onboarding activity
     */
    private void setupOnboarding() {
        kursusTypeFragment = new KursusTypeFragment();
        tagetKursusFragment = new TagetKursusFragment();
        skemaPlaceringFragment = new SkemaPlaceringFragment();
        getStartedFragment = new GetStartedFragment();

        fragmentAdapter.addItem(kursusTypeFragment);
        fragmentAdapter.addItem(tagetKursusFragment);
        fragmentAdapter.addItem(skemaPlaceringFragment);
        fragmentAdapter.addItem(getStartedFragment);
    }

    //TODO: Restore preferences from the onboarding, so that onboarding does not launch all the time.
    private boolean restorePreferences() {
        SharedPreferences sp = getSharedPreferences("Preferences", MODE_PRIVATE);
        return sp.getBoolean("Onboarded",false);
    }

    private void launchMainActivity() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
