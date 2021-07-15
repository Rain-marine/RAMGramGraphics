package gui.controllers.profiles;

import gui.controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.InfoLoader;
import gui.controllers.Toolbar;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import models.Tweet;
import models.User;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PublicProfileGuiController implements Initializable, Controllers {
    @FXML
    private Label info;
    @FXML
    private ImageView profilePhotoImage;

    private static User user;
    private static int previous;


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


    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        switch (previous){
            case (1) -> Toolbar.getInstance().explorer(actionEvent);
            case (2) -> Toolbar.getInstance().timeline(actionEvent);
            case (3) -> System.out.println("your tweets"); //todo
        }
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }


    public void followButtonClicked(ActionEvent actionEvent) {
        NOTIFICATION_CONTROLLER.FollowUser(user);
        FollowingProfileGuiController.setUser(user);
        FollowingProfileGuiController.setPrevious(previous);
        Toolbar.getInstance().changeScene("FXMLs/Profiles/FollowingProfile.fxml",actionEvent);
    }

    public void tweetsButtonClicked(ActionEvent actionEvent) {
        List<Tweet> listOfTweets = TWEET_CONTROLLER.getAllTweets(user);
        //todo
    }

    public void reportButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.reportUser(user);
        AlertBox.display("reported","User reported successfully");
    }

    public void blockButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.blockUser(user);
        BlockedProfileGuiController.setUser(user);
        BlockedProfileGuiController.setPrevious(previous);
        Toolbar.getInstance().changeScene("FXMLs/Profiles/BlockedProfile.fxml",actionEvent);
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        PublicProfileGuiController.user = user;
    }

    public static int getPrevious() {
        return previous;
    }

    public static void setPrevious(int previous) {
        PublicProfileGuiController.previous = previous;
    }
}
