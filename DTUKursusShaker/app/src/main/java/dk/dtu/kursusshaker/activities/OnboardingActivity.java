package dk.dtu.kursusshaker.activities;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import dk.dtu.kursusshaker.R;

public class OnboardingActivity extends AppCompatActivity {

    OnboardingFragmentAdapter fragmentAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    Button buttonBack;
    Button buttonNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

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
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                if (position < fragmentAdapter.getFragmentsList().size()) {
                    position++;
                    viewPager.setCurrentItem(position);
                }
            }
        });

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
    public void updateButtonView(int position) {
        if (position == fragmentAdapter.getFragmentsList().size()-1) {
            buttonNext.setText("asd");
        } else if (position == 0) {
            buttonBack.setVisibility(View.INVISIBLE);
        } else {
            buttonNext.setText("Frem");
            buttonBack.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Adds fragments to the onboarding activity
     */
    public void setupOnboarding() {
        KursusFragment kf = new KursusFragment();
        KursusFragment kf1 = new KursusFragment();
        OnboardingFragment sf = new OnboardingFragment();
        Fragment sf1 = new Fragment();
        fragmentAdapter.addItem(kf, "Kursus");
        fragmentAdapter.addItem(sf1, "Kursus");
        fragmentAdapter.addItem(sf, "Studieretning");
        fragmentAdapter.addItem(kf1, "Kursus");
    }


}
