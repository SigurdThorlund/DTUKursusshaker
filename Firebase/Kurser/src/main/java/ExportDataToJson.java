import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportDataToJson {
    private static String USER_AGENT = "Mozilla/5.0";
    private static String filePath = "database.json";

    public static void main(String argv[]) throws Exception {

        // Array of all the courses as JSONObjects
        JSONArray courses = new JSONArray();

        // The root object of the JSON file
        JSONObject data = new JSONObject();

        // Reads courses from "rensetkurser.csv"
        BufferedReader reader = new BufferedReader(new FileReader("rensetkurser.csv"));
        while (reader.readLine() != null) {

            // Get the course code of the current course
            String id = reader.readLine().substring(0, 5);

            // Get the API result of the course in XML format
            Document doc = loadXMLFromString(getCourse(id));
            doc.getDocumentElement().normalize();

            // Generate a JSONObject for the current course
            NodeList nList = doc.getElementsByTagName("Course");
            JSONObject course = createJSONObject(nList);

            // Add the object to courses if not null
            if (!(course.get("courseCode") == null)) courses.add(course);
        }

        // Put the list of JSONObject courses into a root object
        data.put("courses", courses);

        // Writes to database.json - any existing file will be overwritten
        FileWriter file = new FileWriter(filePath);
        file.write(data.toJSONString());
        file.flush();

    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    static JSONObject createJSONObject(NodeList nList) {
        /* This class takes a NodeList and retrieves the relevant attributes
         * from its elements. It structures the data and saves it in a JSONObject. */

        JSONObject course = new JSONObject();
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                course.put("courseCode", eElement.getAttribute("CourseCode"));

                course.put("danishTitle", eElement.getElementsByTagName("Title")
                        .item(0).getAttributes().getNamedItem("Title").getTextContent());

                course.put("englishTitle", eElement.getElementsByTagName("Title")
                        .item(1).getAttributes().getNamedItem("Title").getTextContent());

                course.put("ects", eElement.getElementsByTagName("Point")
                        .item(0).getTextContent());

                course.put("courseType", eElement.getElementsByTagName("CBS_Programme_Level")
                        .item(0).getAttributes().getNamedItem("CBS_Programme_LevellKey").getTextContent());

                course.put("location", eElement.getElementsByTagName("Location")
                        .item(0).getAttributes().getNamedItem("Key").getTextContent());

                course.put("teachingLanguage", eElement.getElementsByTagName("Teaching_Language")
                        .item(0).getAttributes().getNamedItem("LangCode").getTextContent());

                course.put("mainDepartment", eElement.getElementsByTagName("Main_Dep")
                        .item(0).getAttributes().getNamedItem("UID").getTextContent());

                course.put("schedule", getSchedule(eElement));

                course.put("noCreditPointsWith", getNoPointsWith(eElement));

                course.put("qualifiedPrerequisites", getPrerequisites(eElement, "Qualified_Prerequisites"));

                course.put("mandatoryPrerequisites", getPrerequisites(eElement, "Mandatory_Prerequisites"));

                course.put("previousCourses", getPreviousCourses(eElement));

                course.put("danishContents", getContents(eElement, "da-DK"));

                course.put("englishContents", getContents(eElement, "en-GB"));

                System.out.println(course.get("courseCode") + " Er nu hentet");
            }
        }
        return course;
    }

    static String getContents(Element element, String language) {
        // Returns a short course description in the specified language
        String content = null;

        try {
            NodeList contentList = element.getElementsByTagName("Contents");
            for (int i = 0; i < contentList.getLength(); i++) {
                String lang = contentList.item(i).getParentNode().getAttributes().getNamedItem("Lang").getTextContent();
                if (lang.equals(language)) content = contentList.item(i).getTextContent();
            }
            return content;
        } catch (NullPointerException e) {
            return content;
        }
    }

    static JSONArray getPreviousCourses(Element element) {
        /* Gets a list of previous courses, if any, corresponding to the course,
         * since courses have gone by different names/course codes in the past. */

        JSONArray previousCourses = new JSONArray();

        try {
            NodeList courseList = element.getElementsByTagName("PreviousCourse");
            for (int i = 0; i < courseList.getLength(); i++) {
                previousCourses.add(courseList.item(i).getAttributes().getNamedItem("CourseCode").getTextContent());
            }
            return previousCourses;
        } catch (NullPointerException e) {
            return previousCourses;
        }
    }

    static JSONArray getSchedule(Element element) {
        /* Returns a JSONArray of schedule placements
         If course has several possible placements, e.g. "E4b and January" OR "F4B and August"
         Then the array will contain two subarrays: [["E4B","January"],["F4B","August"]] */
        JSONArray schedule = new JSONArray();

        try {
            NodeList scheduleList = element.getElementsByTagName("Class_Schedule");
            for (int i = 0; i < scheduleList.getLength(); i++) {
                JSONArray scheduleArray = new JSONArray();
                Element scheduleElement = (Element) scheduleList.item(i);
                NodeList nodeList = scheduleElement.getElementsByTagName("Schedule");

                for (int j = 0; j < nodeList.getLength(); j++) {
                    // Puts the schedule key into the schedule array
                    // E.g. if a course has the schedule "E4B and E2", then scheduleArray = ["E4B","E2"]

                    String scheduleString = nodeList.item(j).getAttributes().getNamedItem("ScheduleKey").getTextContent();
                    scheduleArray.add(scheduleString);
                }

                schedule.add(scheduleArray);
            }
            return schedule;
        } catch (NullPointerException e) {
            return schedule;
        }
    }

    static JSONArray getNoPointsWith(Element element) {
        // Returns an array of courses that block for ECTS points for the course (pointspaerring)
        JSONArray noCreditPointsWith = new JSONArray();

        try {
            String noPoints = element.getElementsByTagName("No_Credit_Points_With")
                    .item(0).getAttributes().getNamedItem("CourseCode").getTextContent();
            Pattern pattern = Pattern.compile("\\d{5}");
            Matcher matcher = pattern.matcher(noPoints);
            while (matcher.find()) {
                noCreditPointsWith.add(matcher.group());
            }
            return noCreditPointsWith;
        } catch (NullPointerException e) {
            return noCreditPointsWith;
        }
    }

    static JSONArray getPrerequisites(Element element, String type) {
        /* Returns a list of prerequisites, determined by the string type.
         In the API, the "DTU_CoursesTxt" contains prerequisites in a specific format,
         e.g. "01017/01019.01005/01006/01015/01016": This means that 01017 OR 01019 are required,
         and that 01005 OR 01006 OR 01015 OR 01016 are required.

         In other words, "." means "AND", and "/" means "OR".

         In this method, the above requirements would be reformatted as:
         [["01017","01019"],["01005","01006","01015","01016"]].
         */


        JSONArray prerequisites = new JSONArray();

        try {
            NodeList qualifiedPrerequisitesList = element.getElementsByTagName(type);
            Element qualifiedElement = (Element) qualifiedPrerequisitesList.item(0);
            NodeList nodeList = qualifiedElement.getElementsByTagName("DTU_CoursesTxt");

            // Get the prerequisites in string form
            String prerequisitesString = nodeList
                    .item(0).getAttributes()
                    .getNamedItem("Txt").getTextContent();

            // Split the prerequisites by "AND" first
            String[] prerequisitesStringSplit = prerequisitesString
                    .replaceAll("\\s*/\\s*","/")
                    .split("[ .,]+");


            for (String string : prerequisitesStringSplit) {
                String[] prerequisitearr = string.split("[/]");
                JSONArray prerequisite = new JSONArray();
                for (String string2 : prerequisitearr) {
                    prerequisite.add(string2);
                }
                prerequisites.add(prerequisite);
            }



            /*for (int i = 0; i < prerequisitesStringSplit.length; i++) {
                // Split the prerequisites by "OR"

                JSONArray prerequisite = new JSONArray();
                if (!prerequisitesStringSplit[i].matches(".*[a-zA-Z]+.*")) {
                    String[] array = (prerequisitesStringSplit[i].trim().split("[/]"));

                    for (int j = 0; j < array.length; j++) {
                        if (!array[j].equals("")) prerequisite.add(array[j]);
                    }

                    System.out.println(prerequisite);
                    if (prerequisite.size() > 0) prerequisites.add(prerequisite);
                }
            }*/
            return prerequisites;
        } catch (NullPointerException e) {
            return prerequisites;
        }

    }

    static String getCourse(String id) throws Exception {
        // Returns an XML string of the course information from the API

        String url = "https://kurser.dtu.dk/coursewebservicev2/course.asmx/GetCourse?courseCode=" + id + "&yearGroup=2019/2020";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        TrustModifier.relaxHostChecking(con);
        con.setRequestMethod("GET");

        // Add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        in.close();

        return response.toString();
    }

}