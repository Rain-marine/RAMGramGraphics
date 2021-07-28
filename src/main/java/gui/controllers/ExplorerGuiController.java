package gui.controllers;

import controllers.ProfileAccessController;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import models.Tweet;
import models.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ExplorerGuiController implements Initializable, Controllers {

    @FXML
    private TextField searchField;
    @FXML
    private ScrollPane tweetsArea;

    private List<Tweet> listOfTweets;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listOfTweets = TWEET_CONTROLLER.getTopTweets();
        VBox list = new VBox(10);
        for (Tweet tweet : listOfTweets) {
            list.getChildren().add(new TweetCard(tweet , TweetCard.MODE.EXPLORER).getvBox());
        }
        tweetsArea.setContent(list);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }


    public void searchButtonClicked(ActionEvent actionEvent) {
        String usernameToFind = searchField.getText();
        if (usernameToFind.equals("")) {
            AlertBox.display("Nerd Alert", "You gotta enter a name idiot!");
        } else {
            User user = USER_CONTROLLER.getUserByUsername(usernameToFind);
            if (user == null) {
                AlertBox.display("404", "username not found");
            } else {
                ProfileAccessController profileAccessController = new ProfileAccessController(1, user, 0);
                Toolbar.getInstance().changeScene(profileAccessController.checkAccessibility(), actionEvent);
            }
        }
    }

}
