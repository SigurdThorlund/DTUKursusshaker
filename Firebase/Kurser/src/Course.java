import java.util.ArrayList;

public class Course {
    public Course(){
        // Must have a public no-argument constructor
    }
    public Course(int courseID, String title, String language, double ECTS, int startYear, int endYear, ArrayList<String> schemePlacement, boolean isBoth, boolean isEither, boolean isThreeWeekCourse, boolean isThirteenWeekCourse, boolean isOutsiderNormalScheme, ArrayList<String> courseType) {
        this.courseID = courseID;
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
        this.isOutsiderNormalScheme = isOutsiderNormalScheme;
        this.courseType = courseType;
    }

    int courseID;
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
    boolean isOutsiderNormalScheme;
    ArrayList<String> courseType;
}
