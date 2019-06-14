//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//
//public class ExportData {
//
//    public static void main(String[] args) throws FileNotFoundException {
//        FileInputStream serviceAccount =
//                new FileInputStream("dtu-kurser-firebase-adminsdk-n4mq1-d3c7412d68.json");
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setDatabaseUrl("https://dtu-kurser.firebaseio.com")
//                .build();
//
//        FirebaseApp.initializeApp(options);
//    }
//
//    Course course = new Course();
//}
