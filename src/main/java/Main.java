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

import java.io.IOException;
import java.util.Objects;


public class Main extends Application {
    static Logger log = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
       log.info("Application Started");
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            final SettingController settingsController = new SettingController();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Objects.requireNonNull(ConfigLoader.loadFXML("loginFXMLAddress")))));
            primaryStage.setTitle("RAMGram");
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                boolean answer = SimpleConfirmBox.display("Exit confirmation", "Are you sure to Exit?");
                if (answer) {
                    settingsController.logout();
                    primaryStage.close();
                }
            });
            Image icon = new Image(String.valueOf(getClass().getResource(ConfigLoader.readProperty("appIconAddress"))));
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(Boolean.parseBoolean(ConfigLoader.readProperty("appWindowResizable")));
            primaryStage.show();
        }
        catch (IOException fxmlLoadException){
            System.err.println("FXML URLs configuration is missing");
            log.error("fxml missing: login menu");
        }
    }
}
