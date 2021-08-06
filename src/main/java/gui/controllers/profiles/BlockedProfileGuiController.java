package gui.controllers.profiles;

import controllers.Controllers;
import controllers.ProfileAccessController;
import gui.controllers.*;
import gui.controllers.personalpage.factions.FactionUsersGuiController;
import gui.controllers.popups.AlertBox;
import gui.controllers.tweets.TweetShowerGuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import util.ConfigLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BlockedProfileGuiController implements Initializable , Controllers {

    @FXML
    private ImageView profilePhotoImage;
    @FXML
    private Label info;

    private static long userId;
    private static int previous;
    private static int factionId;
    private static ProfileAccessController profileAccessController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        byte[] byteArray = USER_CONTROLLER.getProfilePhoto(userId);
        Rectangle clip = new Rectangle(
                profilePhotoImage.getFitWidth(), profilePhotoImage.getFitHeight()
        );
        clip.setArcWidth(1000);
        clip.setArcHeight(1000);
        profilePhotoImage.setClip(clip);
        profilePhotoImage.setImage(ImageController.byteArrayToImage(byteArray));
        info.setText(InfoLoader.load(userId));
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        switch (previous){
            case (1) -> SceneLoader.getInstance().explorer(actionEvent);
            case (2) -> SceneLoader.getInstance().timeline(actionEvent);
            case (3) -> SceneLoader.getInstance().yourTweets(actionEvent);
            case (4) -> {
                FactionUsersGuiController.setFactionID(factionId);
                SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("factionUsers"), actionEvent);
            }
        }
    }

    public void unblockButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.unblockUser(userId);
        ProfileAccessController profileAccessController = new ProfileAccessController(ConfigLoader.getPreviousMenuCode("explorer"), userId,0);
        SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(),actionEvent);
    }

    public void tweetsButtonClicked(ActionEvent actionEvent) {
        ArrayList<Long> listOfTweets = TWEET_CONTROLLER.getAllTweets(userId);
        TweetShowerGuiController.setProfileAccessController(profileAccessController);
        TweetShowerGuiController.setListOfTweets(listOfTweets);
        TweetShowerGuiController.setPreviousMenu(ConfigLoader.getPreviousMenuCode("profile"));
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("tweetShower"), actionEvent);
    }

    public void reportButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.reportUser(userId);
        AlertBox.display("reported","User reported successfully");
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public static long getUser() {
        return userId;
    }

    public static void setUser(Long user) {
        BlockedProfileGuiController.userId = user;
    }

    public static int getPrevious() {
        return previous;
    }

    public static void setPrevious(int previous) {
        BlockedProfileGuiController.previous = previous;
    }

    public static int getFactionId() {
        return factionId;
    }

    public static void setFactionId(int factionId) {
        BlockedProfileGuiController.factionId = factionId;
    }

    public static ProfileAccessController getProfileAccessController() {
        return profileAccessController;
    }

    public static void setProfileAccessController(ProfileAccessController profileAccessController) {
        BlockedProfileGuiController.profileAccessController = profileAccessController;
    }
}
