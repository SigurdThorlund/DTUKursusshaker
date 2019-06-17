import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportDataToJson {
    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static String TEST_XML_STRING = "place";
    private static String USER_AGENT = "Mozilla/5.0";

    public static void main(String argv[]) throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader("rensetkurser.csv"));
        ArrayList<String> id = new ArrayList<>();
        while (reader.readLine() != null) {
            id.add(reader.readLine().substring(0, 5));
        }

        Document doc;
        for (String s : id) {
            doc = loadXMLFromString(getCourse(s));
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Course");
            System.out.println(createJSONObject(nList));
        }
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    static JSONObject createJSONObject(NodeList nList) {
        JSONObject course = new JSONObject();
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                course.put("courseCode", eElement.getAttribute("CourseCode"));
//                System.out.println("courseCode : " + course.get("courseCode"));

                course.put("danishTitle", eElement.getElementsByTagName("Title")
                        .item(0).getAttributes().getNamedItem("Title").getTextContent());
//                System.out.println("danishTitle : " + course.get("danishTitle"));

                course.put("englishTitle", eElement.getElementsByTagName("Title")
                        .item(1).getAttributes().getNamedItem("Title").getTextContent());
//                System.out.println("englishTitle : " + course.get("englishTitle"));

                course.put("ects", eElement.getElementsByTagName("Point").item(0).getTextContent());
//                System.out.println("ects : " + course.get("ects"));

                course.put("courseType", eElement.getElementsByTagName("CBS_Programme_Level")
                        .item(0).getAttributes().getNamedItem("CBS_Programme_LevellKey").getTextContent());
//                System.out.println("courseType : " + course.get("courseType"));

                course.put("teachingLanguage", eElement.getElementsByTagName("Teaching_Language")
                        .item(0).getAttributes().getNamedItem("LangCode").getTextContent());
//                System.out.println("teachingLanguage: " + course.get("teachingLanguage"));

                course.put("mainDepartment", eElement.getElementsByTagName("Main_Dep")
                        .item(0).getAttributes().getNamedItem("UID").getTextContent());
//                System.out.println("mainDepartment: " + course.get("mainDepartment"));

                course.put("schedule", getSchedule(eElement));
//                System.out.println("schedulePlacement : " + course.get("schedule").toString());

                course.put("noCreditPointsWith", getNoPointsWith(eElement));
//                System.out.println("noCreditPointsWith : " + course.get("noCreditPointsWith").toString());


                course.put("qualifiedPrerequisites", getPrerequisites(eElement, "Qualified_Prerequisites"));
//                System.out.println("qualifiedPrerequisites: " + course.get("qualifiedPrerequisites").toString());

                course.put("mandatoryPrerequisites", getPrerequisites(eElement, "Mandatory_Prerequisites"));
//                System.out.println("mandatoryPrerequisites: " + course.get("mandatoryPrerequisites").toString());

                course.put("previousCourses", getPreviousCourses(eElement));
//                System.out.println("previousCourses: " + course.get("previousCourses").toString());

                course.put("danishContents", getContents(eElement, "da-DK"));
//                System.out.println("danishContents: " + course.get("danishContents"));

                course.put("englishContents", getContents(eElement, "en-GB"));
//                System.out.println("englishContents: " + course.get("englishContents"));
                System.out.println(course.get("courseCode") + " Er nu hentet");
            }
        }
        return course;
    }

    static String getContents(Element element, String language) {
        String content = null;

        try {
            NodeList contentList = element.getElementsByTagName("Contents");
            for (int i = 0; i < contentList.getLength(); i++) {
                String lang = contentList.item(i).getParentNode().getAttributes().getNamedItem("Lang").getTextContent();
                if (lang.equals(language)) content = contentList.item(i).getTextContent();
            }
        } catch (Exception e) {
        }
        return content;
    }

    static JSONArray getPreviousCourses(Element element) {
        JSONArray jsonArray = new JSONArray();

        try {
            NodeList courseList = element.getElementsByTagName("PreviousCourse");
            for (int i = 0; i < courseList.getLength(); i++) {
                jsonArray.put(courseList.item(i).getAttributes().getNamedItem("CourseCode").getTextContent());
            }
        } catch (Exception e) {
        }
        return jsonArray;
    }

    static JSONArray getSchedule(Element element) {
        // Returns a JSONArray of schedule placements
        // If course has several possible placements, e.g. "E4b and January" OR "F4B and August"
        // Then the array will contain two subarrays: [["E4B","January"],["F4B","August"]]

        JSONArray schedule = new JSONArray();

        try {
            NodeList scheduleList = element.getElementsByTagName("Class_Schedule");
            for (int i = 0; i < scheduleList.getLength(); i++) {
                JSONArray scheduleArray = new JSONArray();
                Element scheduleElement = (Element) scheduleList.item(i);
                NodeList scheduleKey = scheduleElement.getElementsByTagName("Schedule");
                for (int j = 0; j < scheduleKey.getLength(); j++) {
                    String scheduleString = scheduleKey.item(j).getAttributes().getNamedItem("ScheduleKey").getTextContent();
                    scheduleArray.put(scheduleString);
                }
                schedule.put(scheduleArray);
            }
        } catch (Exception e) {
        }
        return schedule;
    }

    static JSONArray getNoPointsWith(Element element) {
        JSONArray noCreditPointsWith = new JSONArray();


        try {
            String noPoints = element.getElementsByTagName("No_Credit_Points_With")
                    .item(0).getAttributes().getNamedItem("CourseCode").getTextContent();
            Pattern pattern = Pattern.compile("\\d{5}");
            Matcher matcher = pattern.matcher(noPoints);
            while (matcher.find()) {
                noCreditPointsWith.put(matcher.group());
            }
        } catch (Exception e) {
        }
        return noCreditPointsWith;
    }

    static JSONArray getPrerequisites(Element element, String type) {
        // Qualified prerequisites:
        // [["02101","02102","02312"],[["02105"]],["01017","01019"]]

        JSONArray qualifiedPrerequisites = new JSONArray();

        try {

            NodeList qualifiedPrerequisitesList = element.getElementsByTagName(type);
            Element qualifiedElement = (Element) qualifiedPrerequisitesList.item(0);
            NodeList comeon = qualifiedElement.getElementsByTagName("DTU_CoursesTxt");

            String prerequisitesString = comeon
                    .item(0).getAttributes()
                    .getNamedItem("Txt").getTextContent();

            String[] prerequisitesStringSplit = prerequisitesString
                    .replace("(", "")
                    .replace(")", "")
                    .split("[\\s.,]+");

            for (int i = 0; i < prerequisitesStringSplit.length; i++) {
                if (!prerequisitesStringSplit[i].matches(".*[a-zA-Z]+.*")) {
                    // TODO: Trim så mellemrum ikke kommer med, på en smart måde
                    qualifiedPrerequisites.put(prerequisitesStringSplit[i].trim().split("[/]"));
                }
            }
        } catch (Exception e) {
        }
        return qualifiedPrerequisites;
    }

    static String getCourse(String id) throws Exception {

        String url = "https://kurser.dtu.dk/coursewebservicev2/course.asmx/GetCourse?courseCode=" + id + "&yearGroup=2019/2020";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        TrustModifier.relaxHostChecking(con);
        con.setRequestMethod("GET");

        //add request header
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

        //print result
        return response.toString();
    }

}