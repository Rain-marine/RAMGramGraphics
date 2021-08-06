package gui.controllers.personalpage.notifications;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Notification;
import util.ConfigLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RequestsToMeGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane notifArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadNotifications();

    }

    private void loadNotifications() {
        List<Notification> followingRequestNotification = NOTIFICATION_CONTROLLER.getFollowingRequestsNotifications();
        if (followingRequestNotification.size() == 0) {
            Label nothing = new Label("You have no requests \nCause everybody hates you\nAnd you look like a monkey");
            notifArea.setContent(nothing);
        }
        else{
            VBox list = new VBox(10);
            for (Notification notification : followingRequestNotification) {
                Label title = new Label( notification.getSender().getUsername() + " wants to follow you");
                Button accept = new Button("Accept");
                accept.setOnAction(event -> {
                    NOTIFICATION_CONTROLLER.acceptFollowRequest(notification);
                    loadNotifications();
                });

                Button reject = new Button("Reject Quietly");
                reject.setOnAction(event -> {
                    NOTIFICATION_CONTROLLER.rejectFollowRequestWithoutNotification(notification);
                    loadNotifications();
                });

                Button rejectAndNotify = new Button("Reject & Notify");
                rejectAndNotify.setOnAction(event -> {
                    NOTIFICATION_CONTROLLER.rejectFollowRequestWithNotification(notification);
                    loadNotifications();
                });

                VBox notifCard = new VBox(5);
                HBox buttons = new HBox(5);
                buttons.getChildren().addAll(accept, reject, rejectAndNotify);
                notifCard.getChildren().addAll(title , buttons);
                list.getChildren().add(notifCard);
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
