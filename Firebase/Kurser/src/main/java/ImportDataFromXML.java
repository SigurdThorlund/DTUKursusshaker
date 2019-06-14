import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.*;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import java.util.ArrayList;

import static org.apache.http.protocol.HTTP.USER_AGENT;

public class ImportDataFromXML {
    public static int PRETTY_PRINT_INDENT_FACTOR = 4;
    public static String TEST_XML_STRING = "place";
    private final String USER_AGENT = "Mozilla/5.0";
//            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
//                    "<root xmlns=\"\">\n" +
//                    "    <CoursePublished>\n" +
//                    "        <CourseXml>\n" +
//                    "            <Course CourseCode=\"01005\" University=\"dtu\" LastUpdated=\"2018-06-21+02:00\" Volume=\"2018/2019\">\n" +
//                    "                <Study_Board Study_BoardKey=\"1\" />\n" +
//                    "                <CBS_Programme_Level CBS_Programme_LevellKey=\"DTU_BSC\" />\n" +
//                    "                <Point PointType=\"ECTS\">20</Point>\n" +
//                    "                <Title Lang=\"da-DK\" Title=\"Matematik 1\" />\n" +
//                    "                <Title Lang=\"en-GB\" Title=\"Advanced Engineering Mathematics 1\" />\n" +
//                    "                <Description_Language Lang=\"da-DK\" />\n" +
//                    "                <Description_Language Lang=\"en-GB\" />\n" +
//                    "                <Teaching_Language LangCode=\"da-DK\" />\n" +
//                    "                <Open_University TaughtUnderOpenUniversity=\"true\" />\n" +
//                    "                <Course_Duration>\n" +
//                    "                    <Exact>P12M</Exact>\n" +
//                    "                </Course_Duration>\n" +
//                    "                <Location Key=\"Campus_Lyngby\" />\n" +
//                    "                <Class_Schedule SortID=\"1\">\n" +
//                    "                    <Schedule_Txt Lang=\"da-DK\" Txt=\"Kurset udbydes på tre forskellige skemaplaceringer afhængig af bachelorlinje. Skema A: E1A, E2 og F1A, F2 Skema B: E3A, E4 og F3A, F4 Skema C: E5, E3B og F5, F3B\" />\n" +
//                    "                    <Schedule_Txt Lang=\"en-GB\" Txt=\"Course is given in three different schedules dependent on study programme.&#xD;&#xA;Scheme A: E1A, E2 og F1A, F2 Scheme B: E3A, E4 og F3A, F4 Scheme C: E5, E3B og F5, F3B\" />\n" +
//                    "                    <Schedule ScheduleKey=\"E\" SortID=\"0\" />\n" +
//                    "                    <Schedule ScheduleKey=\"F\" SortID=\"0\" />\n" +
//                    "                </Class_Schedule>\n" +
//                    "                <Departments>\n" +
//                    "                    <Main_Dep UID=\"1\" University=\"DTU\" Resources=\"100\" KVL_Resources=\"100\" />\n" +
//                    "                </Departments>\n" +
//                    "                <Course_Responsible PersonKey=\"9644\" FUniversity=\"DTU\" SortID=\"0\" DepartmentKey=\"\" Show=\"true\">\n" +
//                    "                    <Description>\n" +
//                    "                        <GivenName>Karsten</GivenName>\n" +
//                    "                        <FamilyName>Schmidt</FamilyName>\n" +
//                    "                        <Email>ksch@dtu.dk</Email>\n" +
//                    "                    </Description>\n" +
//                    "                </Course_Responsible>\n" +
//                    "                <Course_Responsible PersonKey=\"1968\" FUniversity=\"DTU\" SortID=\"1\" DepartmentKey=\"\" Show=\"true\">\n" +
//                    "                    <Description>\n" +
//                    "                        <GivenName>Michael</GivenName>\n" +
//                    "                        <FamilyName>Pedersen</FamilyName>\n" +
//                    "                        <Email>micp@dtu.dk</Email>\n" +
//                    "                    </Description>\n" +
//                    "                </Course_Responsible>\n" +
//                    "                <Examination ExaminationSortID=\"0\" Show=\"true\">\n" +
//                    "                    <Evaluation EvaluationKey=\"GLOB_EXTERN\" />\n" +
//                    "                    <Assessment>\n" +
//                    "                        <Pre_Def_List AssessmentKey=\"Written_Exam_And_Reports\" Show=\"true\" />\n" +
//                    "                        <Supp_Txt Lang=\"da-DK\" Txt=\"4 dele med lige stor vægt: 1) 7 hjemmeopgavesæt, 2) Prøver i efterårspensum, 3) 3-ugers projektrapport med mundtligt forsvar og 4) Prøver i forårsspensum.&#xD;&#xA;Præcis beskrivelse: http://01005.compute.dtu.dk/Info-Eksamensregler\" Show=\"true\" />\n" +
//                    "                        <Supp_Txt Lang=\"en-GB\" Txt=\"4 parts equally weighted: 1) 7 homework assignments, 2) Tests in fall curriculum, 3) 3-week project exercise and 4) Tests in spring curriculum.&#xD;&#xA;Precise description: http://01005.compute.dtu.dk/Info-Eksamensregler\" Show=\"true\" />\n" +
//                    "                    </Assessment>\n" +
//                    "                    <Marking_Scale Marking_ScaleKey=\"GLOB_7Skala\" />\n" +
//                    "                    <Aid AidKey=\"ALL_AID\" AidSortID=\"1\">\n" +
//                    "                        <Aid_Txt Lang=\"en-GB\" Txt=\"For a precise description:&#xD;&#xA;http://01005.compute.dtu.dk/Info-Eksamensregler\" />\n" +
//                    "                        <Aid_Txt Lang=\"da-DK\" Txt=\"Se præcis beskrivelse på:.&#xD;&#xA;http://01005.compute.dtu.dk/Info-Eksamensregler\" />\n" +
//                    "                    </Aid>\n" +
//                    "                    <Date_Of_Exam SortID=\"1\" Show=\"true\">\n" +
//                    "                        <Date_of_Exam_Txt Lang=\"en-GB\" Txt=\"Closing tests: the 9th of Dec. 2018 for the fall and the 14th of May 2019 for the spring\" />\n" +
//                    "                        <Date_of_Exam_Txt Lang=\"da-DK\" Txt=\"Afsluttende prøver: 9.12.2018 for efteråret og 14.05.2019 for foråret\" />\n" +
//                    "                        <Schedule ScheduleKey=\"DTU_DecideWithTeacher\" />\n" +
//                    "                    </Date_Of_Exam>\n" +
//                    "                    <Duration Lang=\"da-DK\" Txt=\"1-times prøver i løbet af semesteret og 3-timers afsluttende prøver\" />\n" +
//                    "                    <Duration Lang=\"en-GB\" Txt=\"1-hour current tests and 3-hours closing tests\" />\n" +
//                    "                </Examination>\n" +
//                    "                <Sign_Up>\n" +
//                    "                    <Place>\n" +
//                    "                        <Pre_Def_List PlaceKey=\"GLOB_CAMPUSNET\" />\n" +
//                    "                    </Place>\n" +
//                    "                </Sign_Up>\n" +
//                    "                <Txt Lang=\"da-DK\">\n" +
//                    "                    <Contents>Lineære ligninger og lineære afbildninger. Matrixalgebra. Vektorrum.  Egenværdiproblemet. Symmetriske og ortogonale matricer. Komplekse tal. Lineære differentialligninger. Elementære funktioner. Funktioner af én og flere reelle variable: linearisering og partielle afledede,Taylors formel og kvadratiske former, ekstrema og niveaukurver, flade-, rum-, og kurve-integral. Vektorfelter, Gauss' og Stokes' sætning. \n" +
//                    "Anvendelse af MAPLE i de ovennævnte emner. Anvendelser i ingeniørvidenskaberne.</Contents>\n" +
//                    "                    <Teaching_And_Learning_Methods>Pr. uge: 2 forelæsninger, 3 timers gruppearbejde/klasseundervisning og 3 timers skemalagt selvstudium. Derudover temaøvelser og projektarbejde i nogle uger.</Teaching_And_Learning_Methods>\n" +
//                    "                    <Course_Objectives>Kursets emner udgør det matematiske grundlag for en lang række tekniske fag og er samtidig basis for videregående studier inden for matematik og anvendt matematik. Et gennemgående tema er linearitet. Målet er at sætte de studerende i stand til at benytte basale matematiske værktøjer, både teoretisk og i anvendelsesorienterede temaøvelser og projekter. Begge aspekter understøttes ved brug af moderne edb-programmer.</Course_Objectives>\n" +
//                    "                    <Remark>Kurset er et to-semesterkursus for bachelor-studerende, som er opdelt i skemagrupper (forelæsningshold) således:\n" +
//                    "Skema A for linjerne Bioteknologi, Kvantitativ Biologi og Sygdomsmodellering , Medicin &amp; Teknologi, Teknisk Biomedicin, Vand, Bioressourcer og Miljømanagement .\n" +
//                    "Skema B for linjerne Byggeteknologi, Bygningsdesign, Fysik &amp; Nanoteknologi, Geofysik &amp; rumteknologi, Matematik &amp; Teknologi, Produktion &amp; Konstruktion. \n" +
//                    "Skema C for linjerne Elektroteknologi, Design og Innovation, Netværksteknologi og IT, Softwareteknologi, Strategisk Analyse og Systemdesign.</Remark>\n" +
//                    "                </Txt>\n" +
//                    "                <Txt Lang=\"en-GB\">\n" +
//                    "                    <Contents>Linear equations and linear maps. Matrix algebra. Vektor spaces. Eigenvalue problems. Symmetric and orthogonal matrices. Complex numbers. Linear differential equations. Standard functions. Functions of one and several real variables: linear approximations and partial derivatives, Taylor expansions and quadratic forms, extrema and level curves, line, surface and volume integrals. Vector fields, Gauss' and Stokes' theorem.\n" +
//                    "Applications of MAPLE in the above areas. Examples of applications in the engineering sciences.</Contents>\n" +
//                    "                    <Teaching_And_Learning_Methods>Per week: 2 lectures, 3h tutorials and 3h groupwork. Moreover thematic exercises and projectwork in some weeks.</Teaching_And_Learning_Methods>\n" +
//                    "                    <Course_Objectives>The course content is the mathematical basis for a broad range of technical fields and also provides a starting point for further studies in mathematics and applied mathematics. The dominating theme of the course is linearity. The goal is to give students the ability to employ fundamental tools of mathematics in theoretical studies as well as in applied thematic exercises and projects. The use of modern computer software supports both of these aspects.</Course_Objectives>\n" +
//                    "                    <Remark>The course is a two-semester course for students following one of the preliminary four-semester programs:\n" +
//                    "Scheme A for the programs Biotechnology, Environmental Engineering, Medicine &amp; Technology, Human Life Science Engineering, Quantitative Biology.\n" +
//                    "Scheme B for the programs Architectural Engineering, Civil Engineering, Earth and Space Physics and Engineering, Mathematics and Technology, Mechanical Engineering and Physics and Nanotechnology.\n" +
//                    "Scheme C for the programs Electrical Engineering, Design and Innovation, Network Technology and IT, Software Technology and Strategic Analysis and Systems Design.</Remark>\n" +
//                    "                </Txt>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Benytte den algebraiske og den geometriske repræsentation af de komplekse tal samt den komplekse eksponentialfunktion.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"use the algeraic and the geometric representation of complex numbers including the complex exponential function.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Benytte matrixregning og Gausselimination i forbindelse med løsning af lineære ligningssystemer.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"use matrix algebra and Gaussian elimination for solving systems of linear equations.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Analysere og forklare løsningsmængder i vektorrum ud fra struktursætningen.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"analyse and explain the linear structure of solution sets in vector spaces.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Udføre simple beregninger med de elementære funktioner, herunder deres inverse.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"perform calculations with the elementary functions including their inverse functions.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Benytte de forskellige varianter af Taylors formel til approksimationer og grænseværdibestemmelse.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"use Taylor's formulas for approximizations and limits.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Løse simple første og anden ordens differentialligninger og differentialligningssytemer.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"solve elementary first and second order differential equations and systems of differential equations.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Beregne ekstrema for funktioner af flere variable, herunder på områder med rand.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"calculate extremas for multivariate functions, especially on domains with boundary.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Parametrisere simple kurver, flader og  rumlige områder, samt beregne simple kurve-, flade- og rumintegraler.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"parameterise elementary curves, surfaces and massive solids and calculate elementary curve, surface and volume integrals. \" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Anvende Gauss' og Stokes sætninger i simple sammenhænge.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"use Gauss's and Stokes's theorems in simple applications.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Anvende matematisk terminologi og ræsonnement i forbindelse med mundtlig og skriftlig fremstilling. \" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"use mathematical terminology and reasoning in oral as well as written presentation.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Organisere samarbejdet i en projektgruppe omkring  matematiske begreber og metoder i en større anvendelsesmæssig sammenhæng.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"coordinate joint work in groups around thematic exercises and applications.\" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <DTU_ObjectiveKeyword SortID=\"1\">\n" +
//                    "                    <Txt Lang=\"da-DK\" Txt=\"Benytte symbolske software-værktøjer, for tiden Maple, til løsning og grafisk illustration af matematiske problemer.\" />\n" +
//                    "                    <Txt Lang=\"en-GB\" Txt=\"use computer algebraic systems (at present Maple) for solving and visualisation of mathematical problems. \" />\n" +
//                    "                </DTU_ObjectiveKeyword>\n" +
//                    "                <Course_Home_Page Lang=\"da-DK\" URL=\"http://01005.compute.dtu.dk/\" />\n" +
//                    "                <DTU>\n" +
//                    "                    <GroenDyst ParticipationKey=\"dtu_groendyst_contact\" />\n" +
//                    "                    <OpenUniversity>\n" +
//                    "                        <SubTitle>Lineær algebra, differentiable funktioner og integralregning i flere variable</SubTitle>\n" +
//                    "                        <AreaOfExpertise AreaOfExpertiseKey=\"14\" />\n" +
//                    "                    </OpenUniversity>\n" +
//                    "                </DTU>\n" +
//                    "                <No_Credit_Points_With CourseCode=\"01006  / 01008 / 01015\" />\n" +
//                    "                <HoursDistribuition>\n" +
//                    "                    <HourDistribuition HourDistribuitionKey=\"LessThan40\" Hours=\"78\" />\n" +
//                    "                    <HourDistribuition HourDistribuitionKey=\"GreaterThanOrEqual40\" Hours=\"60\" />\n" +
//                    "                    <HourDistribuition HourDistribuitionKey=\"Teaching\" Hours=\"138\" />\n" +
//                    "                    <HourDistribuition HourDistribuitionKey=\"SupervisionHours\" Hours=\"33\" />\n" +
//                    "                </HoursDistribuition>\n" +
//                    "                <TeacherTypesDistribuition>\n" +
//                    "                    <TeacherTypeDistribuition TeacherTypeDistribuitionKey=\"PermanentEmployedTeacher\" Percentage=\"44\" />\n" +
//                    "                    <TeacherTypeDistribuition TeacherTypeDistribuitionKey=\"ParttimeEmployedTeacher\" Percentage=\"56\" />\n" +
//                    "                </TeacherTypesDistribuition>\n" +
//                    "                <Record_Info>\n" +
//                    "                    <Action Action=\"Accepted\" PersonKey=\"9411\" PersonUniversity=\"dtu\" Date=\"2018-11-13\" Time=\"21:45:59.053\" />\n" +
//                    "                </Record_Info>\n" +
//                    "            </Course>\n" +
//                    "        </CourseXml>\n" +
//                    "    </CoursePublished>\n" +
//                    "</root>";

    public static void main(String[] args) throws Exception {

        ImportDataFromXML http = new ImportDataFromXML();

        BufferedReader reader = new BufferedReader(new FileReader("rensetkurser.csv"));
        ArrayList<String> id = new ArrayList<String>();
        while (reader.readLine() != null){
           id.add(reader.readLine().substring(0, 5));
        }
        StringBuilder xml = new StringBuilder();
        for (String s : id) {
            xml.append(http.get(s));
        }
        String allXml = xml.toString();
        try {
            FileWriter fw=new FileWriter("kurser2019.json"); //TODO ,true
            JSONObject xmlJSONObj = XML.toJSONObject(allXml);
            String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
            fw.write(jsonPrettyPrintString);
            fw.close();
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
    }

    private String get(String id) throws Exception {

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
