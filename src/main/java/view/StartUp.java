package view;

import controllers.SettingController;
import view.popups.SimpleConfirmBox;
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

public class StartUp {
    static Logger log = LogManager.getLogger(StartUp.class);

    public void onlineInitialize(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(Objects.requireNonNull(ConfigLoader.loadFXML("loginFXMLAddress")))));
            primaryStage.setTitle("RAMGram");
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                boolean answer = SimpleConfirmBox.display("Exit confirmation", "Are you sure to Exit?");
                if (answer) {
                    new SettingController().logout();
                    primaryStage.close();
                }
            });
            Image icon = new Image(String.valueOf(getClass().getClassLoader().getResource(ConfigLoader.readProperty("appIconAddress"))));
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
