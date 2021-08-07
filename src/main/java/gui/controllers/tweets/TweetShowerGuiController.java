package gui.controllers.tweets;

import controllers.ProfileAccessController;
import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TweetShowerGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane tweetsArea;

    private static ArrayList<Long> listOfTweets;
    private static int previousMenu;
    private static ProfileAccessController profileAccessController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox list = new VBox(0);
        list.setPadding(new Insets(0,0,0,0));
        for (Long tweet : listOfTweets) {
            TweetCard.MODE mode = TweetCard.MODE.EXPLORER;
            switch (previousMenu){
                case 1 -> mode = TweetCard.MODE.EXPLORER;
                case 2 -> mode = TweetCard.MODE.TIMELINE;
                case 5 -> mode = TweetCard.MODE.PROFILE;
                case 6 -> mode = TweetCard.MODE.OWNER;
            }
            list.getChildren().add(new TweetCard(tweet, mode).getVBox());
        }
        tweetsArea.setContent(list);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        switch (previousMenu) {
            case 1 -> SceneLoader.getInstance().explorer(actionEvent);
            case 2 -> SceneLoader.getInstance().timeline(actionEvent);
            case 5 -> SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(), actionEvent);
            case 6 -> SceneLoader.getInstance().personalPage(actionEvent);
        }
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }


    public static ArrayList<Long> getListOfTweets() {
        return listOfTweets;
    }

    public static void setListOfTweets(ArrayList<Long> listOfTweets) {
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
