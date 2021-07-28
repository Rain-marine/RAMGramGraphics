package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Tweet;
import org.hibernate.annotations.Fetch;

import java.util.List;

public class TimeLineGuiController implements Controllers{


    @FXML
    private ScrollPane tweetsArea;

    private List<Tweet> listOfTweets;

    public void initialize() {
        VBox list = new VBox(10);
        listOfTweets = TWEET_CONTROLLER.getFollowingTweets();
        for (Tweet tweet : listOfTweets) {
            list.getChildren().add(new TweetCard(tweet , TweetCard.MODE.TIMELINE).getvBox());
        }
        tweetsArea.setContent(list);
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
