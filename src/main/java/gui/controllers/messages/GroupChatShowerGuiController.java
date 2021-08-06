package gui.controllers.messages;

import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import gui.controllers.popups.messaging.AddMemberToGroupChat;
import javax.naming.SizeLimitExceededException;
import java.net.URL;
import java.util.ArrayList;
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
    @FXML
    private Label membersLabel;

    private byte[] chosenImageByteArray = null;

    private static long groupId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CHAT_CONTROLLER.seeChat(groupId);
        groupNameLabel.setText(CHAT_CONTROLLER.getChatName(groupId));
        ArrayList<String> membersNames = CHAT_CONTROLLER.getMembersNames(groupId);
        membersLabel.setText(String.join("\n" , membersNames));
        loadMessages();

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
            CHAT_CONTROLLER.addNewMessageToGroupChat(messageText , chosenImageByteArray , groupId );
            chosenImageView.setImage(null);
            messageTextField.clear();
            loadMessages();
        }
    }

    private void loadMessages() {
        groupNameLabel.setText(CHAT_CONTROLLER.getChatName(groupId));
        VBox list = new VBox(5);
        ArrayList<Long> messageIDs = CHAT_CONTROLLER.getMessages(groupId);
        for (Long messageID : messageIDs) {
            list.getChildren().add(new MessageCard(messageID).getCard());
        }
        messagesArea.setContent(list);
    }

    public static long getGroupId() {
        return groupId;
    }

    public static void setGroupId(long groupId) {
        GroupChatShowerGuiController.groupId = groupId;
    }

    public void addMemberButtonClicked(ActionEvent actionEvent) {
        if (AddMemberToGroupChat.display(groupId))
        {
            ArrayList<String> membersNames = CHAT_CONTROLLER.getMembersNames(groupId);
            membersLabel.setText(String.join("\n" , membersNames));
        }

    }

    public void leaveButtonClicked(ActionEvent actionEvent) {
        CHAT_CONTROLLER.leaveGroup(groupId);
        SceneLoader.getInstance().messaging(actionEvent);

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
