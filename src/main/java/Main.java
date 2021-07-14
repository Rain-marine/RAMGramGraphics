import controllers.SettingController;
import gui.controllers.popups.SimpleConfirmBox;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import util.ConfigLoader;


public class Main extends Application {
    static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
       log.info("Application Started");
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final SettingController settingsController = new SettingController();
        Parent root = FXMLLoader.load(getClass().getResource(ConfigLoader.readProperty("loginFXMLAddress")));
        primaryStage.setTitle("RAMGram");
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            boolean answer = SimpleConfirmBox.display("Exit confirmation" , "Are you sure to Exit?");
            if (answer){
                settingsController.logout();
                primaryStage.close();
            }
        });
        Image icon = new Image(String.valueOf(getClass().getResource(ConfigLoader.readProperty("appIconAddress"))));
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
