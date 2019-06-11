import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ImportDataFromExcel {
    private static final Set<String> LINE_DATA = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "October", "November", "December")));

    public static ArrayList<Course> getCourseArrayList() {
        return courseArrayList;
    }

    private static ArrayList<Course> courseArrayList = new ArrayList<Course>();

    public static void main(String[] args) throws IOException {
        String csvFile = "DTU Kurser-kopi.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        Course course;

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] lineData = line.split(cvsSplitBy);

                String courseID = lineData[2];
                String departmentID = lineData[0];
                String department = lineData[1];
                String title = lineData[3];
                String language = lineData[4];
                double ECTS = Double.parseDouble(lineData[5]);
                int startYear = Integer.parseInt(lineData[6]);
                int endYear = Integer.parseInt(lineData[7]);
                ArrayList<String> schemePlacement = new ArrayList<String>();
                for (int i = 8; i <= 15; i++) {
                    if (!lineData[i].isEmpty()) {
                        schemePlacement.add(lineData[i]);
                    }
                }
                boolean isBoth = !(lineData[14].isEmpty()); // er kurset både forår og efterår?
                boolean isEither = !(lineData[9].isEmpty()); // er kurset enten forår eller efterår
                boolean isThreeWeekCourse = LINE_DATA.contains(lineData[8]);
                boolean isOutsideNormalScheme = lineData[8].equals("Outside schedule structure");
                boolean isThirteenWeekCourse = !isThreeWeekCourse && !isOutsideNormalScheme;
                ArrayList<String> courseType = new ArrayList<String>();
                courseType.add(lineData[16]);
                if (lineData.length == 16) {
                    courseType.add(lineData[17]);
                }

                course = new Course(courseID, departmentID, department, title, language, ECTS, startYear, endYear, schemePlacement, isBoth, isEither, isThreeWeekCourse, isThirteenWeekCourse, isOutsideNormalScheme, courseType);
                courseArrayList.add(course);
                System.out.println(course.toString());
            }

        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
