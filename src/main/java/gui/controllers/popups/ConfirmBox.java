package gui.controllers.popups;

import controllers.SettingController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public interface ConfirmBox {
    SettingController SETTING_CONTROLLER =new SettingController();
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    Image icon = new Image(classloader.getResourceAsStream("Images/warning.png"));

//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,message);
//        alert.show();

}
