package gui.controllers.profiles;

import controllers.ProfileAccessController;
import gui.controllers.*;
import gui.controllers.personalpage.factions.DefaultFactionsGuiController;
import gui.controllers.personalpage.factions.FactionUsersGuiController;
import gui.controllers.popups.AlertBox;
import gui.controllers.tweets.TweetShowerGuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import models.Tweet;
import models.User;
import util.ConfigLoader;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FollowingProfileGuiController implements Initializable, Controllers {

    @FXML
    private Label info;
    @FXML
    private ImageView profilePhotoImage;

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


    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        switch (previous){
            case (1) -> SceneLoader.getInstance().explorer(actionEvent);
            case (2) -> SceneLoader.getInstance().timeline(actionEvent);
            case (3) -> SceneLoader.getInstance().yourTweets(actionEvent);
            case (4) -> {
                switch (factionId){
                    case -1 -> {
                        DefaultFactionsGuiController.setList(DefaultFactionsGuiController.LIST.FOLLOWER);
                        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("defaultFactions"), actionEvent);
                    }
                    case -2 -> {
                        DefaultFactionsGuiController.setList(DefaultFactionsGuiController.LIST.FOLLOWING);
                        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("defaultFactions"), actionEvent);
                    }
                    case -3 ->{
                        DefaultFactionsGuiController.setList(DefaultFactionsGuiController.LIST.BLACKLIST);
                        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("defaultFactions"), actionEvent);
                    }
                    default -> {
                        FactionUsersGuiController.setFactionID(factionId);
                        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("factionUsers"), actionEvent);
                    }
                }

            }
        }
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void unfollowNotify(ActionEvent actionEvent) {
        NOTIFICATION_CONTROLLER.unfollowUserWithNotification(userId);
        ProfileAccessController profileAccessController = new ProfileAccessController(previous,userId,factionId);
        SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(),actionEvent);
    }

    public void unfollowWithoutNotif(ActionEvent actionEvent) {
        NOTIFICATION_CONTROLLER.unfollowUserWithoutNotification(userId);
        ProfileAccessController profileAccessController = new ProfileAccessController(previous,userId,factionId);
        SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(),actionEvent);
    }

    public void messageButtonClicked(ActionEvent actionEvent) {
//        Chat chat = messageController.getChatWithUsername(user.getUsername());
//        if (chat == null)
//            chat = messageController.getChatWithUsername(user.getUsername());
//        new ChatMenu(chat, new FollowingProfile(user, previousMenu)).run();
        //todo
    }

    public void tweetsButtonClicked(ActionEvent actionEvent) {
        ArrayList<Long> listOfTweets = TWEET_CONTROLLER.getAllTweets(userId);
        TweetShowerGuiController.setProfileAccessController(profileAccessController);
        TweetShowerGuiController.setListOfTweets(listOfTweets);
        TweetShowerGuiController.setPreviousMenu(5);
        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("tweetShower"), actionEvent);
    }

    public void reportButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.reportUser(userId);
        AlertBox.display("reported","User reported successfully");
    }

    public void blockButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.blockUser(userId);
        BlockedProfileGuiController.setUser(userId);
        BlockedProfileGuiController.setPrevious(previous);
        BlockedProfileGuiController.setFactionId(factionId);
        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("blockedProf"),actionEvent);
    }

    public static long getUser() {
        return userId;
    }

    public static void setUser(long user) {
        FollowingProfileGuiController.userId = user;
    }

    public static int getPrevious() {
        return previous;
    }

    public static void setPrevious(int previous) {
        FollowingProfileGuiController.previous = previous;
    }

    public static int getFactionId() {
        return factionId;
    }

    public static void setFactionId(int factionId) {
        FollowingProfileGuiController.factionId = factionId;
    }

    public static ProfileAccessController getProfileAccessController() {
        return profileAccessController;
    }

    public static void setProfileAccessController(ProfileAccessController profileAccessController) {
        FollowingProfileGuiController.profileAccessController = profileAccessController;
    }
}
