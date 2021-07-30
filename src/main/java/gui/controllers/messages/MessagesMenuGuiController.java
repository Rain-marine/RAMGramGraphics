package gui.controllers.messages;

import gui.controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MessagesMenuGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane chatsArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }


    public void newChatButtonClicked(ActionEvent actionEvent) {
    }

    public void newGroupButtonClicked(ActionEvent actionEvent) {
    }

    public void savedMessagesButtonClicked(ActionEvent actionEvent) {
    }

    public void savedTweetsButtonClicked(ActionEvent actionEvent) {
    }
}
