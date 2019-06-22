package dk.dtu.kursusshaker.data;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PrimaryViewModel extends ViewModel {
    private ArrayList<Course> basketCourseList = new ArrayList<>();

    private int countCalled = 0;

    public void callViewModel() {
        countCalled++;
    }

    public int getCallViewModelCount() {
        return countCalled;
    }

    // add selected course to basket arrayList
    public boolean addCourseToBasketArrayList(Course course) {
        if (!basketCourseList.contains(course)) {
            basketCourseList.add(course);
            return true;
        } else {
            return false;
        }
    }

    public int getSizeOfBasketArrayList() {
        return basketCourseList.size();
    }
}
