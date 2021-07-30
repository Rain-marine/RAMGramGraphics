package gui.controllers;

import controllers.ChatController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Tweet;

import java.util.List;

public class ChatMenuGuiController implements Controllers{

    @FXML
    private ScrollPane chatsArea;

    private List<Tweet> listOfChats;

    public void initialize() {
        VBox list = new VBox(10);
        listOfChats = ;
        for (Tweet tweet : listOfChats) {
            list.getChildren().add(new TweetCard(tweet , TweetCard.MODE.TIMELINE).getvBox());
        }
        chatsArea.setContent(list);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }
}
