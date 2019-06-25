package dk.dtu.kursusshaker.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashSet;

import dk.dtu.kursusshaker.R;
import dk.dtu.kursusshaker.data.Course;

public class ViewCourseActivity extends AppCompatActivity {
    Course course;
    TextView courseNameTextView;
    TextView courseCodeTextView;
    TextView courseBodyTextView;
    TextView infoSchemePlacement;
    TextView infoNoPoints;
    TextView infoprereq;
    ChipGroup chipGroup;
    ChipGroup chipGroupPrereq;
    ChipGroup chipGroupPoints;
    MaterialButton addToBasketButton;

    HashSet<String> takenCourses;

    public ViewCourseActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        Intent intent = getIntent();
        course = (Course) intent.getSerializableExtra("selectedCourse");
        setTextViews();
    }

    private void setTextViews() {
        courseCodeTextView = findViewById(R.id.courseNumber);
        courseCodeTextView.setText(course.getCourseCode());

        courseNameTextView = findViewById(R.id.courseHeadText);
        courseNameTextView.setText(course.getDanishTitle());

        courseBodyTextView = findViewById(R.id.CourseBodyText);
        courseBodyTextView.setText(course.getDanishContents());

        infoNoPoints = findViewById(R.id.no_points_with);
        chipGroupPoints = findViewById(R.id.chip_group_points);

        infoprereq = findViewById(R.id.prereq_text);
        chipGroupPrereq = findViewById(R.id.chip_group_pereq);

        addToBasketButton = findViewById(R.id.addToBasketButton);
        addToBasketButton.setOnClickListener(addToBasketClickListener); // onAddToBasket click listener

        chipGroup = findViewById(R.id.chip_group);

        infoSchemePlacement = findViewById(R.id.scheme_placement);
        if (course.getSchedule()[0].length > 1) {
            infoSchemePlacement.setText("Skemaplaceringer");
        }
        for (int i = 0; i < course.getSchedule()[0].length; i++) {
            Chip chip = new Chip(chipGroup.getContext());
            chip.setClickable(false);
            if (course.getSchedule()[0][i].contains("OutsideSchedule")) {
                chip.setText("Uden for normalt skema");
            } else {
                chip.setText(course.getSchedule()[0][i]);
            }
            chipGroup.addView(chip);
        }


        if (course.getNoCreditPointsWith().length == 0) {
            infoNoPoints.setText("");
            infoNoPoints.setHeight(0);
            chipGroupPoints.setChipSpacingVertical(0);
        }

        for (int i = 0; i < course.getNoCreditPointsWith().length; i++) {
            Chip chip = new Chip(chipGroupPoints.getContext());
            chip.setClickable(false);
            chip.setText(course.getNoCreditPointsWith()[i]);
            chipGroupPoints.addView(chip);
        }

        if (course.getMandatoryPrerequisites().length != 0 || course.getQualifiedPrerequisites().length != 0) {
            infoprereq.setText("Du burde have haft følgende fag");
        }
        if (course.getQualifiedPrerequisites().length > 0) { //TODO: virker okay, men ittererer ikke over hele arrayet. 11320 har begge slags
            QUAL_LOOP:
            for (int i = 0; i < course.getQualifiedPrerequisites().length; i++) {
                int counter = 0;
                for (int j = 0; j < course.getQualifiedPrerequisites()[0].length; j++) {
                    Chip chip = new Chip(chipGroupPrereq.getContext());
                    chip.setClickable(false);
                    chip.setText(course.getQualifiedPrerequisites()[i][j]);
                    chipGroupPrereq.addView(chip);
                    counter++;
                    if (course.getQualifiedPrerequisites()[i].length == counter) {
                        break QUAL_LOOP;
                    }
                }
            }
        }
        if (course.getMandatoryPrerequisites().length > 0) {
            MAND_LOOP:
            for (int i = 0; i < course.getMandatoryPrerequisites().length; i++) {
                int counter = 0;
                for (int j = 0; j < course.getMandatoryPrerequisites()[0].length; j++) {
                    Chip chip = new Chip(chipGroupPrereq.getContext());
                    chip.setClickable(false);
                    chip.setText(course.getMandatoryPrerequisites()[i][j]);
                    chipGroupPrereq.addView(chip);
                    counter++;
                    if (course.getMandatoryPrerequisites()[i].length == counter) {
                        break MAND_LOOP;
                    }
                }
            }
        }
    }

    View.OnClickListener addToBasketClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("returnedCourse", course);
            setResult(1,returnIntent);
            finish();
        }
    };

    // When user press back he will return back to primaryActivity with a resultCode 2
    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(2, returnIntent);
        finish();
    }

}
