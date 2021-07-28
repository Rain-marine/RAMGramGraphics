package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import models.Tweet;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TweetShowerGuiController implements Initializable {

    @FXML
    private ScrollPane tweetsArea;

    private static List<Tweet> listOfTweets;
    private static int previousMenu;
    private static long profileOwnerId;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox list = new VBox(10);
        for (Tweet tweet : listOfTweets) {
            list.getChildren().add(new TweetCard(tweet , TweetCard.MODE.PROFILE).getvBox());
        }
        tweetsArea.setContent(list);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        switch (previousMenu){
            case 5 -> System.out.println("profile") ;
            case 6 -> System.out.println("self");
            case 7 -> System.out.println("parent comment");
        }
        //todo
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
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

    public static long getProfileOwnerId() {
        return profileOwnerId;
    }

    public static void setProfileOwnerId(long profileOwnerId) {
        TweetShowerGuiController.profileOwnerId = profileOwnerId;
    }
}
