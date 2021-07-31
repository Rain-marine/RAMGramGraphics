package gui.controllers.messages;

import gui.controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupChatShowerGuiController implements Initializable, Controllers {

    @FXML
    private Label groupNameLabel;
    @FXML
    private ScrollPane messagesArea;

    private static long groupId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().messaging(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void sendButtonClicked(ActionEvent actionEvent) {
    }


}
