package gui.controllers.tweets;

import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import gui.controllers.popups.SimpleConfirmBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.naming.SizeLimitExceededException;

public class NewTweetGuiController implements Controllers {

    @FXML
    private TextField tweetText;
    @FXML
    private ImageView image;

    private byte[] tweetImage = null;

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().personalPage(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void postButtonClicked(ActionEvent actionEvent) {
        if (!tweetText.getText().equals("") || tweetImage != null){
            boolean answer = SimpleConfirmBox.display("confirmation" , "Are you sure?");
            if (answer){
                TWEET_CONTROLLER.addTweet(tweetText.getText(),tweetImage);
                AlertBox.display("Done!", "Tweet posted");
                tweetText.setText("");
            }

        }
        else{
            AlertBox.display("Nerd Alert", "you haven't written anything");
        }
    }

    public void addImageButtonClicked(ActionEvent actionEvent) {
        try {
            tweetImage = ImageController.pickImage();
            image.setImage(ImageController.byteArrayToImage(tweetImage));
        } catch (SizeLimitExceededException e) {
            AlertBox.display("too large!", "image size is too large");
        }
    }
}
