import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ExportDataToJson {

    public static void main(String argv[]) {

        try {

            File fXmlFile = new File("testCourse.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Course");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("courseCode : " + eElement.getAttribute("CourseCode"));
                    System.out.println("danishTitle : " + eElement.getElementsByTagName("Title").item(0).getAttributes().getNamedItem("Title").getTextContent());
                    System.out.println("ects : " + eElement.getElementsByTagName("Point").item(0).getTextContent());
                    System.out.println("englishTitle : " + eElement.getElementsByTagName("Title").item(1).getAttributes().getNamedItem("Title").getTextContent());
                    System.out.println("courseType : " + eElement.getElementsByTagName("CBS_Programme_Level").item(0).getAttributes().getNamedItem("CBS_Programme_LevellKey").getTextContent());
                    ArrayList<String> schedule = new ArrayList<>();
                    for (int i = 0; i < eElement.getElementsByTagName("Schedule").getLength(); i++){
                        if (eElement.getElementsByTagName("Schedule").item(i).getAttributes().getNamedItem("SortID") != null){ // Da eksamen har samme format, tjekkes om SortID eksisterer, det gør den ikke i eksamensplacering.
                            schedule.add(eElement.getElementsByTagName("Schedule").item(i).getAttributes().getNamedItem("ScheduleKey").getTextContent());
                        }
                    }
                    System.out.println("schedulePlacement : " + schedule.toString());
//                    ArrayList<String> objectiveDanish = new ArrayList<>();
//                    ArrayList<String> objectiveEnglish = new ArrayList<>();
//                    for (int i = 0; i < eElement.getElementsByTagName("DTU_ObjectiveKeyword").getLength(); i++){
////                        objectiveDanish.add(eElement.getElementsByTagName("Schedule").item(i).getAttributes().getNamedItem("ScheduleKey").getTextContent());
//                        System.out.println(eElement.getElementsByTagName("DTU_ObjectiveKeyword").item(i).toString());
//                        objectiveDanish.add(eElement.getElementsByTagName("DTU_ObjectiveKeyword").item(i).getAttributes().item(0).getAttributes().getNamedItem("Txt").getTextContent());
//                    }
//                    System.out.println("objectiveDanish : " + objectiveDanish.toString());
                    String noPoints = eElement.getElementsByTagName("No_Credit_Points_With").item(0).getAttributes().getNamedItem("CourseCode").getTextContent();
                    System.out.println("noPointsWith : " + Arrays.toString(noPoints.split(".")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}