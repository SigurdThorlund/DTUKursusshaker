package dk.dtu.kursusshaker.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class OnBoardingViewModel extends ViewModel {


    // Boolean to keep track of onBoarding state
    private boolean onboardingInProgress = false;

    // List containing courses already taken by the student
    // the list is filled elements during onBoarding
    private ArrayList<Course> finishedCoursesList = new ArrayList<>();

    private int countCalled = 0;

    public void callViewModel() {
        countCalled++;
    }

    public int getCallViewModelCount() {
        return countCalled;
    }

    // add finished courses to an arrayList -> TO-BE used in filtering
    public boolean addFinishedCourseToArrayList(Course course) {
        if (!finishedCoursesList.contains(course)) {
            finishedCoursesList.add(course);
            return true;
        } else {
            return false;
        }
    }

    // OnBoarding state functions
    public void setOnboardingInProgress(Boolean bool) {
        this.onboardingInProgress = bool;
    }
    public Boolean getOnBoardingInProgress() {
        return onboardingInProgress;
    }


}
