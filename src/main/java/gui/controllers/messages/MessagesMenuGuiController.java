package gui.controllers.messages;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import gui.controllers.popups.messaging.NewGroup;
import gui.controllers.popups.messaging.SendNewMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import util.ConfigLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MessagesMenuGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane chatsArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadChats();
    }

    private void loadChats() {
        ChatShowerGuiController.setPreviousMenu(ChatShowerGuiController.PREVIOUS.DEFAULT);
        List<Long> chatIds = CHAT_CONTROLLER.getChatsIds();
        if (chatIds.size() == 0){
            chatsArea.setContent(new Label("you have no chats"));
        }
        else{
            VBox list = new VBox(10);
            for (Long chatId : chatIds) {
                list.getChildren().add(new ChatCard(chatId).getCard());
            }
            chatsArea.setContent(list);
        }
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
        SendNewMessage.display();
        SceneLoader.getInstance().messaging(actionEvent);
    }

    public void newGroupButtonClicked(ActionEvent actionEvent) {
        NewGroup.display();
        SceneLoader.getInstance().messaging(actionEvent);
    }

    public void savedMessagesButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("savedMessages"), actionEvent);
    }

    public void savedTweetsButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("savedTweets"), actionEvent);
    }
}
