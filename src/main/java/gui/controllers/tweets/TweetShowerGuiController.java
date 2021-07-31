package gui.controllers.tweets;

import controllers.ProfileAccessController;
import gui.controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Tweet;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TweetShowerGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane tweetsArea;

    private static List<Tweet> listOfTweets;
    private static int previousMenu;
    private static ProfileAccessController profileAccessController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox list = new VBox(10);
        for (Tweet tweet : listOfTweets) {
            TweetCard.MODE mode = TweetCard.MODE.EXPLORER;
            switch (previousMenu){
                case 1 -> mode = TweetCard.MODE.EXPLORER;
                case 2 -> mode = TweetCard.MODE.TIMELINE;
                case 5 -> mode = TweetCard.MODE.PROFILE;
                case 6 -> mode = TweetCard.MODE.OWNER;
            }
            list.getChildren().add(new TweetCard(tweet, mode).getvBox());
        }
        tweetsArea.setContent(list);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        System.out.println(previousMenu);
        switch (previousMenu) {
            case 1 -> SceneLoader.getInstance().explorer(actionEvent);
            case 2 -> SceneLoader.getInstance().timeline(actionEvent);
            case 5 -> SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(), actionEvent);
            case 6 -> SceneLoader.getInstance().personalPage(actionEvent);
        }
        //todo
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }


    public static List<Tweet> getListOfTweets() {
        return listOfTweets;
    }

    public static void setListOfTweets(List<Tweet> listOfTweets) {
        TweetShowerGuiController.listOfTweets = listOfTweets;
    }

    public static int getPreviousMenu() {
        return previousMenu;
    }

    public static void setPreviousMenu(int previousMenu) {
        TweetShowerGuiController.previousMenu = previousMenu;
    }

    public static ProfileAccessController getProfileAccessController() {
        return profileAccessController;
    }

    public static void setProfileAccessController(ProfileAccessController profileAccessController) {
        TweetShowerGuiController.profileAccessController = profileAccessController;
    }
}