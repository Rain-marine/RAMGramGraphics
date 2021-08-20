package view.popups;

import controllers.SettingController;
import javafx.scene.image.Image;

public interface ConfirmBox {
    SettingController SETTING_CONTROLLER =new SettingController();
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    Image icon = new Image(classloader.getResourceAsStream("Images/warning.png"));

//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,message);
//        alert.show();

}
