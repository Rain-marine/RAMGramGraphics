package gui.controllers.messages;

import controllers.ChatController;
import gui.controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
    @FXML
    private Button sendBtn;
    @FXML
    private Button choosePicBtn;
    @FXML
    private TextField massageTextField;

    private static long userChatId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox list = new VBox(5);
        ArrayList<Long> messageIDs = CHAT_CONTROLLER.getMessages(userChatId);
        for (Long messageID : messageIDs) {
            list.getChildren().add(new MessageCard(messageID).getCard());
        }
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

    public void choosePicButtonClicked(ActionEvent actionEvent) {
    }
}
