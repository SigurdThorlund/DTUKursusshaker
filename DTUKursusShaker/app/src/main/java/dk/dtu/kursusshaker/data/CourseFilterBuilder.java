package dk.dtu.kursusshaker.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CourseFilterBuilder {
    /* This class builds an array of filtered courses based on the filters given as parameters in
    the constructor.*/

    String season;
    String[] scheduleFilter;
    String[] completed;
    String[] teachingLanguages;
    String[] locations;
    String[] departments;
    String type;
    String[] ects;

    CoursesAsObject coursesAsObject;
    Course[] allCourses;
    ArrayList<Course> filteredCourses;


    public CourseFilterBuilder(CoursesAsObject coursesAsObject, String season,
                               String[] scheduleFilter, String[] completed,
                               String[] teachingLanguages, String[] locations, String type,
                               String[] departments, String[] ects) {
        this.season = season;
        this.scheduleFilter = scheduleFilter;
        this.completed = completed;
        this.teachingLanguages = teachingLanguages;
        this.locations = locations;
        this.type = type;
        this.departments = departments;
        this.ects = ects;

        this.coursesAsObject = coursesAsObject;
        this.allCourses = coursesAsObject.getCourseArray();
        this.filteredCourses = new ArrayList<>(Arrays.asList(allCourses));
    }

    public ArrayList<Course> filterAllCourses() {
        filterCourse(coursesAsObject.getCourseFromId("02155"));
        for (Course course : allCourses) {
            filterCourse(course);
        }

        return filteredCourses;
    }

    public void filterCourse(Course course) {
        if (Arrays.asList(completed).contains(course.getCourseCode()) ||
                !scheduleMeetsPreferences(course.getSchedule()) ||
                !allPrerequisitesAreMet(completed, course) ||
                prerequisiteIsMet(course.getNoCreditPointsWith()) ||
                !filterLanguage(course) ||
                !filterLocation(course) ||
                !filterType(course) ||
                !filterDepartments(course) ||
                !filterEcts(course)) {
            filteredCourses.remove(course);
        }
    }

    private boolean filterType(Course course) {
        // Returns true if the user is allowed to take this course.
        /* DTU_BSC: Can take BTU_MSC and DTU_MSC courses
         DTU_DIPLOM: Can take all courses
         DTU_MSC: Can only take DTU_MSC courses*/

        String courseType = course.getCourseType();
        if (type.equals("DTU_BSC")) {
            if (courseType.equals("DTU_BSC") || courseType.equals("DTU_MSC")) return true;
        }

        if (type.equals("DTU_MSC")) {
            if (courseType.equals("DTU_MSC")) return true;
        }

        if (type.equals("DTU_DIPLOM")) return true;

        return false;
    }

    private boolean filterDepartments(Course course) {
        // Returns true if the course's main department (institute) is in the user's preferences
        // E.g. "1" = compute
        if (departments.length == 0) return true;
        return Arrays.asList(departments).contains(course.getMainDepartment());
    }

    private boolean filterEcts(Course course) {
        // Returns true if the course's ects points are in the user's preferences
        if (ects.length == 0) return true;
        return Arrays.asList(ects).contains(course.getEcts());
    }

    private boolean filterLocation(Course course) {
        /* Returns true if the course's main location fits with the user's preferences
         Locations are "DTU_LYNGBY" and "DTU_BALLERUP"*/
        if (locations.length == 0) return true;
        return Arrays.asList(locations).contains(course.getLocation());
    }

    private boolean filterLanguage(Course course) {
        /* Returns true if the course's teaching language fits with the user's preference
         Languages are either "da-DK" or "en-GB"*/
        if (teachingLanguages.length == 0) return true;
        return Arrays.asList(teachingLanguages).contains(course.getTeachingLanguage());
    }

    private boolean allPrerequisitesAreMet(String[] completedCourses, Course course) {
        /* Returns true if at least one course in each prerequisite array is completed
         There are mandatory and qualified preferences, but they are treated equally*/

        String[][] mandatoryPrerequisites = course.getMandatoryPrerequisites();
        String[][] qualifiedPrerequisites = course.getQualifiedPrerequisites();

        // If the user has no completed courses, return true if there are no prerequisites for the course
        if (completedCourses.length == 0) {
            return mandatoryPrerequisites.length + qualifiedPrerequisites.length == 0;
        }

        for (String[] prerequisite : qualifiedPrerequisites) {
            if (!prerequisiteIsMet(prerequisite)) return false;
        }

        for (String[] prerequisite : mandatoryPrerequisites) {
            if (!prerequisiteIsMet(prerequisite)) return false;
        }

        return true;
    }

    private String[] getAliases(Course course) {
        /* Returns an array of possible aliases of a course. Some courses have existed under previous
         names, or they are identical to another course.*/

        String[] noPointsWith = course.getNoCreditPointsWith();
        String[] previousNames = course.getPreviousCourses();

        // Collect all equivalent courses
        Set<String> set = new HashSet<>();
        set.add(course.getCourseCode());
        if (noPointsWith != null) set.addAll(Arrays.asList(noPointsWith));
        if (previousNames != null) set.addAll(Arrays.asList(previousNames));

        // Return as String array
        System.out.println(course.getCourseCode() + " has aliases: " + Arrays.toString(set.toArray(new String[0])));
        return set.toArray(new String[0]);
    }

    // TODO: samarbejde ml. prerequisites og aliases, se 02155's krav - man behøves ikke overholde alle krav!
    private boolean prerequisiteIsMet(String[] courseArray) {
        // Returns true if at least one course in the array has been completed by the user
        for (String course : courseArray) {
            System.out.println(course);
            Course courseAsObject = coursesAsObject.getCourseFromId(course);
            String[] aliases;

            if (courseAsObject != null) aliases = getAliases(coursesAsObject.getCourseFromId(course));
            else aliases = new String[]{course};

            for (String string : aliases) {
                System.out.println(Arrays.toString(completed) + " contains " + string + ": ");

                if (Arrays.asList(completed).contains(string)) {
                    System.out.print("true");
                    return true;
                }
                System.out.print("false");
            }
        }

        return false;
    }

    private boolean scheduleMeetsPreferences(String[][] courseSchedule) {
        /* Returns true if at least one of the possible schedules fits to the user's preferences.
         This is because some courses have several schedules, e.g. [E4B, January] OR [F4B, June].*/

        if (courseSchedule.length == 0) return true;
        if (scheduleFilter.length == 0) return true;

        // Combines the season ("E" or "F") with the schedule placements (e.g. "4A" or "2B")
        String[] seasonalScheduleFilter = new String[scheduleFilter.length];
        int i = 0;
        for (String placement : scheduleFilter) {
            seasonalScheduleFilter[i] = season + scheduleFilter[i];
            i++;
        }

        // Checks if one of the possible schedules of the courses fits to the user's preferences
        for (String[] schedules : courseSchedule) {
            if (Arrays.asList(seasonalScheduleFilter).containsAll(Arrays.asList(schedules)))
                return true;

        }

        return false;
    }

    public ArrayList<Course> getFilteredCourses() {
        // Gets the filtered courses
        return filteredCourses;
    }
}