package dk.dtu.kursusshaker.data;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class PrimaryViewModel extends ViewModel {
    private CourseFilterBuilder courseFilterBuilder;

    public void setCourseFilterBuilder(CourseFilterBuilder courseFilterBuilder) {
        this.courseFilterBuilder = courseFilterBuilder;
    }

    public CourseFilterBuilder getCourseFilterBuilder() {
        return courseFilterBuilder;
    }

    private ArrayList<Course> basketCourseList = new ArrayList<>();

    private Course recommendedCourse;

    public void setRecommendedCourse(Course course) {
        recommendedCourse = course;
    }

    public Course getRecommendedCourse() {
        return recommendedCourse;
    }

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
