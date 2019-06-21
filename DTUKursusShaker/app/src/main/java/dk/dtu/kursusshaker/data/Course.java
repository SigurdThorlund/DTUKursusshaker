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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getEnglishContents() {
        return englishContents;
    }

    public void setEnglishContents(String englishContents) {
        this.englishContents = englishContents;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDanishTitle() {
        return danishTitle;
    }

    public void setDanishTitle(String danishTitle) {
        this.danishTitle = danishTitle;
    }

    public String getEcts() {
        return ects;
    }

    public void setEcts(String ects) {
        this.ects = ects;
    }

    public String getDanishContents() {
        return danishContents;
    }

    public void setDanishContents(String danishContents) {
        this.danishContents = danishContents;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMainDepartment() {
        return mainDepartment;
    }

    public void setMainDepartment(String mainDepartment) {
        this.mainDepartment = mainDepartment;
    }

    public String getTeachingLanguage() {
        return teachingLanguage;
    }

    public void setTeachingLanguage(String teachingLanguage) {
        this.teachingLanguage = teachingLanguage;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public String[][] getSchedule() {
        return schedule;
    }

    public void setSchedule(String[][] schedule) {
        this.schedule = schedule;
    }

    public String[][] getMandatoryPrerequisites() {
        return mandatoryPrerequisites;
    }

    public void setMandatoryPrerequisites(String[][] mandatoryPrerequisites) {
        this.mandatoryPrerequisites = mandatoryPrerequisites;
    }

    public String[] getPreviousCourses() {
        return previousCourses;
    }

    public void setPreviousCourses(String[] previousCourses) {
        this.previousCourses = previousCourses;
    }

    public String[][] getQualifiedPrerequisites() {
        return qualifiedPrerequisites;
    }

    public void setQualifiedPrerequisites(String[][] qualifiedPrerequisites) {
        this.qualifiedPrerequisites = qualifiedPrerequisites;
    }

    public String[] getNoCreditPointsWith() {
        return noCreditPointsWith;
    }

    public void setNoCreditPointsWith(String[] noCreditPointsWith) {
        this.noCreditPointsWith = noCreditPointsWith;
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
