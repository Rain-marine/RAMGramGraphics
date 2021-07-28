package gui.controllers.profiles;

import controllers.ProfileAccessController;
import controllers.SettingController;
import controllers.TweetController;
import controllers.UserController;
import gui.controllers.*;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import models.LoggedUser;
import models.Tweet;
import models.User;
import views.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class BlockedProfileGuiController implements Initializable , Controllers {

    @FXML
    private ImageView profilePhotoImage;
    @FXML
    private Label info;

    private static User user;
    private static int previous;
    private static int factionId;
    private static ProfileAccessController profileAccessController;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        byte[] byteArray = user.getProfilePhoto();
        Rectangle clip = new Rectangle(
                profilePhotoImage.getFitWidth(), profilePhotoImage.getFitHeight()
        );
        clip.setArcWidth(1000);
        clip.setArcHeight(1000);
        profilePhotoImage.setClip(clip);
        profilePhotoImage.setImage(ImageController.byteArrayToImage(byteArray));
        info.setText(InfoLoader.load(user));
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        switch (previous){
            case (1) -> Toolbar.getInstance().explorer(actionEvent);
            case (2) -> Toolbar.getInstance().timeline(actionEvent);
            case (3) -> System.out.println("your tweets"); //todo
            case (4) -> System.out.println("factions"); //todo
        }
    }

    public void unblockButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.unblockUser(user);
        ProfileAccessController profileAccessController = new ProfileAccessController(1,user,0);
        Toolbar.getInstance().changeScene(profileAccessController.checkAccessibility(),actionEvent);
    }

    public void tweetsButtonClicked(ActionEvent actionEvent) {
        List<Tweet> listOfTweets = TWEET_CONTROLLER.getAllTweets(user);
        TweetShowerGuiController.setProfileAccessController(profileAccessController);
        TweetShowerGuiController.setListOfTweets(listOfTweets);
        TweetShowerGuiController.setPreviousMenu(5);
        Toolbar.getInstance().changeScene("FXMLs/TweetShower.fxml" , actionEvent);
    }

    public void reportButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.reportUser(user);
        AlertBox.display("reported","User reported successfully");
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        BlockedProfileGuiController.user = user;
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
