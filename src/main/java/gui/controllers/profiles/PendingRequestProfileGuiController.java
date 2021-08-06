package gui.controllers.profiles;

import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.InfoLoader;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import util.ConfigLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class PendingRequestProfileGuiController implements Initializable, Controllers {
    @FXML
    private Label info;
    @FXML
    private ImageView profilePhotoImage;

    private static long userId;
    private static int previous;


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
        }
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }


    public void removeReqButtonClicked(ActionEvent actionEvent) {
        NOTIFICATION_CONTROLLER.deleteRequest(userId);
        PrivateProfileGuiController.setPrevious(previous);
        PrivateProfileGuiController.setUser(userId);
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("privateProf"),actionEvent);
    }

    public void reportButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.reportUser(userId);
        AlertBox.display("reported","User reported successfully");
    }

    public void blockButtonClicked(ActionEvent actionEvent) {
        USER_CONTROLLER.blockUser(userId);
        BlockedProfileGuiController.setUser(userId);
        BlockedProfileGuiController.setPrevious(previous);
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("blockedProf"),actionEvent);
    }

    public static long getUser() {
        return userId;
    }

    public static void setUser(long user) {
        PendingRequestProfileGuiController.userId = user;
    }

    public static int getPrevious() {
        return previous;
    }

    public static void setPrevious(int previous) {
        PendingRequestProfileGuiController.previous = previous;
    }


}
