import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.xml.XmlMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    public Course(){
        // Must have a public no-argument constructor
    }

    public Course(String coursePublished, String courseID, String departmentID, String department, String title, String language, double ECTS, int startYear, int endYear, ArrayList<String> schemePlacement, boolean isBoth, boolean isEither, boolean isThreeWeekCourse, boolean isThirteenWeekCourse, boolean isOutsideNormalScheme, ArrayList<String> courseType) {
        CoursePublished = coursePublished;
        this.courseID = courseID;
        this.departmentID = departmentID;
        this.department = department;
        this.title = title;
        this.language = language;
        this.ECTS = ECTS;
        this.startYear = startYear;
        this.endYear = endYear;
        this.schemePlacement = schemePlacement;
        this.isBoth = isBoth;
        this.isEither = isEither;
        this.isThreeWeekCourse = isThreeWeekCourse;
        this.isThirteenWeekCourse = isThirteenWeekCourse;
        this.isOutsideNormalScheme = isOutsideNormalScheme;
        this.courseType = courseType;
    }

    String CoursePublished;
    String courseID;
    String departmentID;
    String department;
    String title;
    String language;
    double ECTS;
    int startYear;
    int endYear;
    ArrayList<String> schemePlacement;
    boolean isBoth; // er kurset både forår og efterår?
    boolean isEither; // er kurset enten forår eller efterår
    boolean isThreeWeekCourse;
    boolean isThirteenWeekCourse;
    boolean isOutsideNormalScheme;
    ArrayList<String> courseType;

    @Override
    public String toString() {
        return "Course{" +
                "courseID='" + courseID + '\'' +
                ", departmentID='" + departmentID + '\'' +
                ", department='" + department + '\'' +
                ", title='" + title + '\'' +
                ", language='" + language + '\'' +
                ", ECTS=" + ECTS +
                ", startYear=" + startYear +
                ", endYear=" + endYear +
                ", schemePlacement=" + schemePlacement +
                ", isBoth=" + isBoth +
                ", isEither=" + isEither +
                ", isThreeWeekCourse=" + isThreeWeekCourse +
                ", isThirteenWeekCourse=" + isThirteenWeekCourse +
                ", isOutsideNormalScheme=" + isOutsideNormalScheme +
                ", courseType=" + courseType +
                '}';
    }


    public static void main(String[] args) throws IOException {
        File xml = new File("testCourse.xml");
        XmlMapper xmlMapper = new XmlMapper();
        Course course = xmlMapper.readValue(xml, Course.class);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(course);
        System.out.println(json);
    }


}
