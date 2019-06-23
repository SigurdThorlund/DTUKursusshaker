package dk.dtu.kursusshaker.data;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class CoursesAsObject {

    Course[] courseArray;

    public CoursesAsObject(Context context) {
        Gson gson = new Gson();

        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open("database.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Reader reader = new InputStreamReader(is);
        // Convert JSON File to Java Object
        courseArray = gson.fromJson(reader, Course[].class);



    }

    public Course[] getCourseArray() {
        return courseArray;
       /* //TODO: gør til simpel getter

        // Returns the full list of courses if there are no filters (filters = excluded courses)
        if (excludedCourses.size() == 0) return courseArray;

        // Initializes new array for the filtered courses
        Course[] filteredArray = new Course[courseArray.length - excludedCourses.size()];
        int originalCourseCounter = 0;
        int excludedCounter = 0;
        int filteredCounter = 0;


        // Copy all the courses which aren't in excludedCourses into the new array.
        for (Course course : courseArray) {
            // If every excluded course has been checked, copy the rest of the courseArray into the
            // filteredArray.
            if (excludedCounter == excludedCourses.size()) {
                System.arraycopy(courseArray, originalCourseCounter,
                        filteredArray, filteredCounter,
                        courseArray.length - originalCourseCounter);
                return filteredArray;
            }

            // Copy the current course to the new array, unless it should be excluded
            if (!excludedCourses.contains(course.getCourseCode())) {
                filteredArray[filteredCounter++] = course;
            } else excludedCounter++;

            originalCourseCounter++;
        }

        // Returns the filtered course list
        return filteredArray;*/
    }

    public  Course getCourseFromId(String id){
        Course tempCourse = new Course();
        for (Course c:courseArray
             ) {
            if (c.getCourseCode().equals(id)){
                tempCourse = c;
            }
        }
        return tempCourse;
    }


    public static void main(String[] args) throws FileNotFoundException {

    }
}

