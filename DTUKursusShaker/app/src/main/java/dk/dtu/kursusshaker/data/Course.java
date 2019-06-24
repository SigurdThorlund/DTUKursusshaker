package dk.dtu.kursusshaker.data;

import java.io.Serializable;
import java.util.Arrays;

public class Course implements Serializable {
    private String courseType;
    private String englishContents;
    private String courseCode;
    private String danishTitle;
    private String ects;
    private String danishContents;
    private String location;
    private String mainDepartment;
    private String teachingLanguage;
    private String englishTitle;
    private String[][] schedule;
    private String[][] mandatoryPrerequisites;
    private String[] previousCourses;
    private String[][] qualifiedPrerequisites;
    private String[] noCreditPointsWith;

    public String getEcts() {
        return ects;
    }

    public String getMainDepartment() {
        return mainDepartment;
    }

    public String getCourseType() {
        return courseType;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getDanishTitle() {
        return danishTitle;
    }

    public String getDanishContents() {
        return danishContents;
    }

    public String getLocation() {
        return location;
    }

    public String getTeachingLanguage() {
        return teachingLanguage;
    }

    public String[][] getSchedule() {
        return schedule;
    }

    public String[][] getMandatoryPrerequisites() {
        return mandatoryPrerequisites;
    }

    public String[] getPreviousCourses() {
        return previousCourses;
    }

    public String[][] getQualifiedPrerequisites() {
        return qualifiedPrerequisites;
    }


    public String[] getNoCreditPointsWith() {
        return noCreditPointsWith;
    }


    @Override
    public String toString() {
        return "Course{" +
                "courseType='" + courseType + '\'' +
                ", englishContents='" + englishContents + '\'' +
                ", courseCode='" + courseCode + '\'' +
                ", danishTitle='" + danishTitle + '\'' +
                ", ects='" + ects + '\'' +
                ", danishContents='" + danishContents + '\'' +
                ", location='" + location + '\'' +
                ", mainDepartment='" + mainDepartment + '\'' +
                ", teachingLanguage='" + teachingLanguage + '\'' +
                ", englishTitle='" + englishTitle + '\'' +
                ", schedule=" + Arrays.toString(schedule) +
                ", mandatoryPrerequisites=" + Arrays.toString(mandatoryPrerequisites) +
                ", previousCourses=" + Arrays.toString(previousCourses) +
                ", qualifiedPrerequisites=" + Arrays.toString(qualifiedPrerequisites) +
                ", noCreditPointsWith=" + Arrays.toString(noCreditPointsWith) +
                '}';
    }

}
