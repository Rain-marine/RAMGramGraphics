package gui.controllers.messages;

import gui.controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ChatShowerGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane messagesArea;
    @FXML
    private ImageView profileImageview;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label lastSeenLabel;


    private static long userChatId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox list = new VBox(5);
        MessageCard messageCard = new MessageCard(1233L);
        messageCard.getCard();
        //for -> list.getChildren.add (messageCard.getCard());

        messagesArea.setContent(list);
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

    public static long getUserChatId() {
        return userChatId;
    }

    public static void setUserChatId(long userChatId) {
        ChatShowerGuiController.userChatId = userChatId;
    }

    public void sendButtonClicked(ActionEvent actionEvent) {
    }
}
