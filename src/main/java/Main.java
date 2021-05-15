import controllers.SettingController;
import gui.controllers.popups.ConfirmBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class Main extends Application {
    static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
       log.info("Application Started");
//        MenuManager manager = new MenuManager(new Scanner(System.in));
//        manager.run();
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final SettingController settingsController = new SettingController();
        Parent root = FXMLLoader.load(getClass().getResource("Welcome/Login.fxml"));
        primaryStage.setTitle("RAMGram");
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            boolean answer = ConfirmBox.display("Exit confirmation" , "Are you sure to Exit?");
            if (answer){
                settingsController.logout();
                primaryStage.close();
            }
        });

        primaryStage.getIcons().add(new Image("file:resources/Images/icon.jpg"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
