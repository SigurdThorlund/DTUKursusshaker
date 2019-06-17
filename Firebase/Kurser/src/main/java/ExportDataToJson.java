import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExportDataToJson {

    public static void main(String argv[]) throws ParserConfigurationException, IOException, SAXException {

        File fXmlFile = new File("testCourse.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("Course");
        System.out.println(createJSONObject(nList));
    }

    static JSONObject createJSONObject(NodeList nList) {
        JSONObject course = new JSONObject();
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                course.put("courseCode", eElement.getAttribute("CourseCode"));
                System.out.println("courseCode : " + course.get("courseCode"));

                course.put("danishTitle", eElement.getElementsByTagName("Title")
                        .item(0).getAttributes().getNamedItem("Title").getTextContent());
                System.out.println("danishTitle : " + course.get("danishTitle"));

                course.put("englishTitle", eElement.getElementsByTagName("Title")
                        .item(1).getAttributes().getNamedItem("Title").getTextContent());
                System.out.println("englishTitle : " + course.get("englishTitle"));

                course.put("ects", eElement.getElementsByTagName("Point").item(0).getTextContent());
                System.out.println("ects : " + course.get("ects"));

                course.put("courseType", eElement.getElementsByTagName("CBS_Programme_Level")
                        .item(0).getAttributes().getNamedItem("CBS_Programme_LevellKey").getTextContent());
                System.out.println("courseType : " + course.get("courseType"));

                course.put("teachingLanguage", eElement.getElementsByTagName("Teaching_Language")
                        .item(0).getAttributes().getNamedItem("LangCode").getTextContent());
                System.out.println("teachingLanguage: " + course.get("teachingLanguage"));

                course.put("mainDepartment", eElement.getElementsByTagName("Main_Dep")
                        .item(0).getAttributes().getNamedItem("UID").getTextContent());
                System.out.println("mainDepartment: " + course.get("mainDepartment"));

                course.put("schedule", getSchedule(eElement));
                System.out.println("schedulePlacement : " + course.get("schedule").toString());

                course.put("noCreditPointsWith", getNoPointsWith(eElement));
                System.out.println("noCreditPointsWith : " + course.get("noCreditPointsWith").toString());


                course.put("qualifiedPrerequisites", getPrerequisites(eElement,"Qualified_Prerequisites"));
                System.out.println("qualifiedPrerequisites: " + course.get("qualifiedPrerequisites").toString());

                course.put("mandatoryPrerequisites", getPrerequisites(eElement,"Mandatory_Prerequisites"));
                System.out.println("mandatoryPrerequisites: " + course.get("mandatoryPrerequisites").toString());

                course.put("previousCourses", getPreviousCourses(eElement));
                System.out.println("previousCourses: " + course.get("previousCourses").toString());

                course.put("danishContents", getContents(eElement, "da-DK"));
                System.out.println("danishContents: " + course.get("danishContents"));

                course.put("englishContents", getContents(eElement, "en-GB"));
                System.out.println("englishContents: " + course.get("englishContents"));

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
        } catch (Exception e) {}
        return content;
    }

    static JSONArray getPreviousCourses(Element element) {
        JSONArray jsonArray = new JSONArray();

        try {
            NodeList courseList = element.getElementsByTagName("PreviousCourse");
            for (int i = 0; i < courseList.getLength(); i++) {
                jsonArray.put(courseList.item(i).getAttributes().getNamedItem("CourseCode").getTextContent());
            }
        } catch (Exception e) {}
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
        } catch (Exception e) {}
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
        } catch (Exception e) { }
        return qualifiedPrerequisites;
    }

}