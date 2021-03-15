package revlfinal;

/**
 * Created by Coleten McGuire on 5/14/2016.
 */
import com.kinvey.nativejava.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
    public static Client mKinveyClient;
    public static final String nameOfCollection = "VideoGames";
    public static Stage pStage;
    public static Scene ourScene;

    //starts application
    @Override
    public void start(Stage primaryStage) throws Exception {
        pStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("ReVL");
        ourScene = new Scene(root, 1300, 690);
        primaryStage.setScene(ourScene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    //connects to kinvey
    public static void main(String[] args) {
        mKinveyClient = new Client.Builder("kid_ZyBaA-MgAe", "4e43176fdfe14d44878492851d93385f").build();
        try {
            mKinveyClient.user().loginBlocking("kid_ZyBaA-MgAe", "81181f297d044117a537c53378eae68d").execute();
            System.out.println("Client logged in -> " + mKinveyClient.user().isUserLoggedIn());
        } catch (IOException e) {
            System.out.println("Couldn't login -> " + e);
            e.printStackTrace();
        }
        launch(args);
    }
}