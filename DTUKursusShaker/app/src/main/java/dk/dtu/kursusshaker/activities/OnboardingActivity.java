package dk.dtu.kursusshaker.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import dk.dtu.kursusshaker.R;

public class OnboardingActivity extends AppCompatActivity {

    OnboardingFragmentAdapter fragmentAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        fragmentAdapter = new OnboardingFragmentAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.fragment_viewpage);
        viewPager.setAdapter(fragmentAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        setupOnboarding();
    }

    public void setupOnboarding() {
        KursusFragment kf = new KursusFragment();
        OnboardingFragment sf = new OnboardingFragment();
        fragmentAdapter.addItem(kf, "Kursus");
        fragmentAdapter.addItem(sf, "Studieretning");
    }


}
