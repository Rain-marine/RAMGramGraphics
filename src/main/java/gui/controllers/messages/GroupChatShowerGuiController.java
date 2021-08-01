package gui.controllers.messages;

import gui.controllers.Controllers;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class GroupChatShowerGuiController implements Initializable, Controllers {

    @FXML
    private Label groupNameLabel;
    @FXML
    private ScrollPane messagesArea;
    @FXML
    private TextField messageTextField;
    @FXML
    private ImageView chosenImageView;

    private byte[] chosenImageByteArray = null;

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
        String messageText = messageTextField.getText();
        if (messageText.equals("") && chosenImageByteArray == null){
            AlertBox.display("Nerd Alert" , "write something idiot");
        }
        else {
            CHAT_CONTROLLER.addMessageToChat(chatId,messageText , chosenImageByteArray );
            chosenImageView.setImage(null);
            messageTextField.clear();
            loadMessages();
        }
    }

    public static long getGroupId() {
        return groupId;
    }

    public static void setGroupId(long groupId) {
        GroupChatShowerGuiController.groupId = groupId;
    }
}
