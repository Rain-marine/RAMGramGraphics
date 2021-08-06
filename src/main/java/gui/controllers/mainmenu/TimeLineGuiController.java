package gui.controllers.mainmenu;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import gui.controllers.tweets.TweetCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class TimeLineGuiController implements Controllers {


    @FXML
    private ScrollPane tweetsArea;

    private ArrayList<Long> listOfTweets;

    public void initialize() {
        VBox list = new VBox(0);
        listOfTweets = TWEET_CONTROLLER.getFollowingTweets();
        for (long tweet : listOfTweets) {
            list.getChildren().add(new TweetCard(tweet , TweetCard.MODE.TIMELINE).getVBox());
        }
        tweetsArea.setContent(list);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }
}
