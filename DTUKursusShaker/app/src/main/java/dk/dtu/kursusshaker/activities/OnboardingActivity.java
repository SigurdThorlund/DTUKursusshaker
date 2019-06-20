package dk.dtu.kursusshaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import dk.dtu.kursusshaker.MainActivity;
import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.fragments.KursusTypeFragment;
import dk.dtu.kursusshaker.fragments.SkemaPlaceringFragment;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        context = getApplicationContext();

        //Connect adapter, tablayout and viewpager.
        fragmentAdapter = new OnboardingFragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.fragment_viewpage);
        viewPager.setAdapter(fragmentAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

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

        //Listener for when the fragment is on the last view.
        nextLastViewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchMainActivity();
            }
        };

        buttonNext.setOnClickListener(nextDefaultListener);

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
        if (isLastView(position)) {
            buttonNext.setText(R.string.button_done);
            buttonNext.setOnClickListener(nextLastViewListener);
        } else if (position == 0) {
            buttonBack.setVisibility(View.INVISIBLE);
        } else {
            buttonNext.setText(R.string.button_next);
            buttonNext.setOnClickListener(nextDefaultListener);
            buttonBack.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param position
     * @return true if the fragment is the last view in the viewpager
     */
    private boolean isLastView(int position) {
        return position == fragmentAdapter.getFragmentsList().size()-1;
    }

    /**
     * Adds fragments to the onboarding activity
     */
    private void setupOnboarding() {
        KursusTypeFragment kursusTypeFragment = new KursusTypeFragment();
        SkemaPlaceringFragment skemaPlaceringFragment = new SkemaPlaceringFragment();
        fragmentAdapter.addItem(kursusTypeFragment);
        fragmentAdapter.addItem(skemaPlaceringFragment);
    }

    //TODO: Restore preferences from the onboarding, so that onboarding does not launch all the time.
    private boolean restorePreferences() {
        return true;
    }

    private void launchMainActivity() {
        Intent intent = new Intent(OnboardingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
