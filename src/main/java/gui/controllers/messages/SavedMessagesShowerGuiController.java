package gui.controllers.messages;

import controllers.ProfileAccessController;
import gui.controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import javax.naming.SizeLimitExceededException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SavedMessagesShowerGuiController implements Initializable, Controllers {

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
    private TextField messageTextField;
    @FXML
    private ImageView chosenImageView;

    private byte[] chosenImageByteArray = null;

    public enum PREVIOUS { DEFAULT , PROFILE}

    private static long chatId;
    private static ChatShowerGuiController.PREVIOUS previousMenu;
    private static long previousProfileId;
    private static ProfileAccessController profileAccessController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CHAT_CONTROLLER.seeChat(chatId);
        loadMessages();
    }

    private void loadMessages() {
        VBox list = new VBox(5);
        ArrayList<Long> messageIDs = MESSAGE_CONTROLLER.getSavedMessage();
        for (Long messageID : messageIDs) {
            list.getChildren().add(new MessageCard(messageID).getCard());
        }
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

    public void choosePicButtonClicked(ActionEvent actionEvent) {
        try {
            chosenImageByteArray = ImageController.pickImage();
            if (chosenImageByteArray != null){
                chosenImageView.setImage(ImageController.byteArrayToImage(chosenImageByteArray));
            }
        } catch (SizeLimitExceededException e) {
            AlertBox.display("too large" , "Image size too large");
        }
    }

}
