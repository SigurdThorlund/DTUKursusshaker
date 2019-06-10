import java.util.ArrayList;

public class ExportData {
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

public ExportData() {
    this.courseID = courseID;

}
