package gui.controllers.personalpage;

import controllers.DateFormat;
import controllers.UserController;
import controllers.Controllers;
import gui.controllers.ImageController;
import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import models.LoggedUser;
import util.ConfigLoader;

import javax.naming.SizeLimitExceededException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class PersonalPageGuiController implements Initializable , Controllers {


    @FXML
    private javafx.scene.control.TextField usernameTextField;
    @FXML
    private javafx.scene.control.TextField nameTextField;
    @FXML
    private javafx.scene.control.TextField emailTextField;
    @FXML
    private javafx.scene.control.TextField phoneNumberTextField;
    @FXML
    private Label birthdayTextLabel;
    @FXML
    private javafx.scene.control.TextField bioTextField;
    @FXML
    private Label usernameEdit;
    @FXML
    private Label phoneNumberEdit;
    @FXML
    private Label emailEdit;
    @FXML
    private ImageView profilePhotoImage;


    private String username;
    private String fullName;
    private String bio;
    private String phoneNumber;
    private String email;
    private Date birthday;
    private UserController userController = new UserController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Loading personal page");
        loadInfo();
        usernameTextField.setFocusTraversable(false);
        nameTextField.setFocusTraversable(false);
        emailTextField.setFocusTraversable(false);
        phoneNumberTextField.setFocusTraversable(false);
        bioTextField.setFocusTraversable(false);

    }

    private void loadInfo() {
        username = LoggedUser.getLoggedUser().getUsername();
        fullName = LoggedUser.getLoggedUser().getFullName();
        email = LoggedUser.getLoggedUser().getEmail();
        phoneNumber = LoggedUser.getLoggedUser().getPhoneNumber();
        bio = LoggedUser.getLoggedUser().getBio();
        birthday = LoggedUser.getLoggedUser().getBirthday();
        byte[] byteArray = LoggedUser.getLoggedUser().getProfilePhoto();

        Rectangle clip = new Rectangle(
                profilePhotoImage.getFitWidth(), profilePhotoImage.getFitHeight()
        );
        clip.setArcWidth(1000);
        clip.setArcHeight(1000);
        profilePhotoImage.setClip(clip);

        profilePhotoImage.setImage(ImageController.byteArrayToImage(byteArray));
        usernameTextField.setText(username);
        nameTextField.setText(fullName);
        emailTextField.setText(email);
        phoneNumberTextField.setText(phoneNumber.equals("") ? "not set" : phoneNumber);
        bioTextField.setText(bio);
        birthdayTextLabel.setText(birthday == null ? "not set" : DateFormat.dayMonthYear(birthday));
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void updateInfoButtonClicked(ActionEvent actionEvent) {
        System.out.println("updating info...");
        usernameEdit.setText("");
        emailEdit.setText("");
        phoneNumberEdit.setText("");
        String newUsername = usernameTextField.getText();
        if (!newUsername.equals(username)) {
            if (!userController.ChangeUsername(newUsername)) {
                usernameEdit.setText("username already exists");
            }
        }

        String newEmail = emailTextField.getText();
        if (!newEmail.equals(email)) {
            if (!newEmail.contains("@") || !newEmail.contains(".")) {
                emailEdit.setText("invalid email address");
            } else if (!userController.changeEmail(newEmail)) {
                usernameEdit.setText("email already exists");
            }
        }

        String newName = nameTextField.getText();
        if (!newName.equals(fullName)) {
            userController.changeName(newName);
        }

        String newNumber = phoneNumberTextField.getText();
        try {
            Integer.parseInt(newNumber);
            if (!newNumber.equals(phoneNumber)) {
                if (!userController.changeNumber(newNumber)) {
                    emailEdit.setText("phone number already exists");
                }
            }

        } catch (NumberFormatException e) {
            if (!(phoneNumber.equals(""))) {
                phoneNumberEdit.setText("invalid format");
            }
        }

        String newBio = bioTextField.getText();
        if (!newBio.equals(bio)) {
            userController.changeBio(newBio);
        }
        LoggedUser.update();
        loadInfo();


    }

    public void newTweetButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("newTweet"), actionEvent);
    }

    public void yourTweetsButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().yourTweets(actionEvent);
    }

    public void factionsButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("factionList"), actionEvent);
    }

    public void notificationButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("notificationAdd"),actionEvent);
    }


    public void changePhotoButtonClicked(ActionEvent actionEvent) {
        byte[] byteArray;
        try {
            byteArray = ImageController.pickImage();
            userController.changeProfilePhoto(byteArray);
            LoggedUser.update();
            loadInfo();
        } catch (SizeLimitExceededException e) {
            AlertBox.display("size limit error","Image size is too large. \nImage size should be less than 2MB");
        }
    }
}
