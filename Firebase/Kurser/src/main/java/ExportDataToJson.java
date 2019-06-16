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

public class ExportDataToJson {

    public static void main(String argv[]) throws ParserConfigurationException, IOException, SAXException {

        File fXmlFile = new File("testCourse.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("Course");
        createJSONObject(nList);
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


                course.put("schedule", getSchedule(eElement));
                System.out.println("schedulePlacement : " + course.get("schedule").toString());

                course.put("noCreditPointsWith", getNoPointsWith(eElement));
                System.out.println("noCreditPointsWith : " + course.get("noCreditPointsWith").toString());


                course.put("qualifiedPrerequisites", getQualifiedPrerequisites(eElement));
                System.out.println("qualifiedPrerequisites: " + course.get("qualifiedPrerequisites").toString());

                course.put("mandatoryPrerequisites", getMandatoryPrerequisites(eElement));
                System.out.println("mandatoryPrerequisites: " + course.get("mandatoryPrerequisites").toString());

                // TODO: previous courses
                // TODO: institut
                // TODO: contents:
                               /*course.put("danishContents",eElement.getElementsByTagName("Contents")
                    .item(0).getAttributes().getNamedItem("Contents").getTextContent());
                    System.out.println("danishContents " + course.get("danishContents"));

                    course.put("englishContents",eElement.getElementsByTagName("Contents")
                            .item(1).getAttributes().getNamedItem("Contents").getTextContent());*/


/*                    for (int i = 0; i < eElement.getElementsByTagName("DTU_ObjectiveKeyword").getLength(); i++){
//                        objectiveDanish.add(eElement.getElementsByTagName("Schedule").item(i).getAttributes().getNamedItem("ScheduleKey").getTextContent());
                        System.out.println(eElement.getElementsByTagName("DTU_ObjectiveKeyword").item(i).toString());
                        objectiveDanish.add(eElement.getElementsByTagName("DTU_ObjectiveKeyword").item(i).getAttributes().item(0).getAttributes().getNamedItem("Txt").getTextContent());
                    }
                    System.out.println("objectiveDanish : " + objectiveDanish.toString());*/

            }
        }
        return course;
    }

    static JSONArray getSchedule(Element element) {
        // Returns a JSONArray of schedule placements
        // If course has several possible placements, e.g. "E4b and January" OR "F4B and August"
        // Then the array will contain two subarrays: [["E4B","January"],["F4B","August"]]

        JSONArray schedule = new JSONArray();

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
        return schedule;
    }

    static JSONArray getNoPointsWith(Element element) {
        JSONArray noCreditPointsWith = new JSONArray();

        try {
            String noPoints = element.getElementsByTagName("No_Credit_Points_With")
                    .item(0).getAttributes().getNamedItem("CourseCode").getTextContent();
            String[] noCreditPointsWithString = noPoints.split("-|\\.");
            for (int i = 0; i < noCreditPointsWithString.length; i++) {
                noCreditPointsWith.put(noCreditPointsWithString[i]);
            }
        } catch (Exception e) {
        }
        return noCreditPointsWith;
    }

    static JSONArray getMandatoryPrerequisites(Element element) {
        // Mandatory prerequisites
        JSONArray mandatoryPrerequisites = new JSONArray();

        try {

            NodeList mandatoryPrerequisitesList = element.getElementsByTagName("Mandatory_Prerequisites");
            Element mandatoryElement = (Element) mandatoryPrerequisitesList.item(0);
            NodeList comeon = mandatoryElement.getElementsByTagName("DTU_CoursesTxt");

            String prerequisitesString = comeon
                    .item(0).getAttributes()
                    .getNamedItem("Txt").getTextContent();

            String[] prerequisitesStringSplit = prerequisitesString
                    .replace("(", "")
                    .replace(")", "")
                    .split("[.]");

            for (int i = 0; i < prerequisitesStringSplit.length; i++) {
                if (!prerequisitesStringSplit[i].matches(".*[a-zA-Z]+.*")) {
                    // TODO: Trim så mellemrum ikke kommer med, på en smart måde
                    mandatoryPrerequisites.put(prerequisitesStringSplit[i].trim().split("[/]"));
                }
            }
        } catch (Exception e) { }
        return mandatoryPrerequisites;
    }

    static JSONArray getQualifiedPrerequisites(Element element) {
        // Qualified prerequisites:
        // [["02101","02102","02312"],[["02105"]],["01017","01019"]]

        JSONArray qualifiedPrerequisites = new JSONArray();

        try {

            NodeList qualifiedPrerequisitesList = element.getElementsByTagName("Qualified_Prerequisites");
            Element qualifiedElement = (Element) qualifiedPrerequisitesList.item(0);
            NodeList comeon = qualifiedElement.getElementsByTagName("DTU_CoursesTxt");

            String prerequisitesString = comeon
                    .item(0).getAttributes()
                    .getNamedItem("Txt").getTextContent();

            String[] prerequisitesStringSplit = prerequisitesString
                    .replace("(", "")
                    .replace(")", "")
                    .split("[.]");

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