package gui.controllers.personalpage.notifications;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Notification;
import util.ConfigLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyRequestsGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane notifArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Notification> notifications = NOTIFICATION_CONTROLLER.getYourFollowingRequestNotification();
        if (notifications.size() == 0) {
            Label nothing = new Label("You haven't sent any new request!");
            notifArea.setContent(nothing);
        } else {
            VBox list = new VBox(10);
            for (Notification notification : notifications) {
                Label info = new Label(notification.getReceiver().getUsername());
                list.getChildren().add(info);
            }
            notifArea.setContent(list);
        }
    }


    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("notificationAdd"),actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }
}
