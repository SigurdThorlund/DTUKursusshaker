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

/**
 * Helper class to access coursedata
 *
 */
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
    }

    public Course getCourseFromId(String id) {
        Course tempCourse = new Course();
        for (Course c : courseArray) {
            if (c.getCourseCode().equals(id)) {
                return c;
            }
        }
        return null;
    }


    public static void main(String[] args) throws FileNotFoundException {

    }
}

