package gui.controllers;

import controllers.DateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import models.LoggedUser;

import java.awt.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class PersonalPageGuiController implements Initializable {


    @FXML
    private javafx.scene.control.TextField usernameTextField;
    @FXML
    private javafx.scene.control.TextField nameTextField;
    @FXML
    private javafx.scene.control.TextField emailTextField;
    @FXML
    private javafx.scene.control.TextField phoneNumberTextField;
    @FXML
    private javafx.scene.control.TextField birthdayTextField;
    @FXML
    private javafx.scene.control.TextField bioTextField;


    private String username;
    private String fullName;
    private String bio;
    private String phoneNumber;
    private String email;
    private Date birthday;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Loading personal page");
        username = LoggedUser.getLoggedUser().getUsername();
        fullName = LoggedUser.getLoggedUser().getFullName();
        email = LoggedUser.getLoggedUser().getEmail();
        phoneNumber = LoggedUser.getLoggedUser().getPhoneNumber();
        bio = LoggedUser.getLoggedUser().getBio();
        birthday = LoggedUser.getLoggedUser().getBirthday();

        usernameTextField.setText(username);
        nameTextField.setText(fullName);
        emailTextField.setText(email);

        phoneNumberTextField.setText(phoneNumber.equals("") ? "not set" :  phoneNumber);
        bioTextField.setText(bio.equals("")? "not set" :  bio);
        birthdayTextField.setText(birthday == null ? "not set" : DateFormat.dayMonthYear(birthday));
    }

    public void backButtonClicked(ActionEvent actionEvent) {

    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
    }

    public void updateInfoButtonClicked(ActionEvent actionEvent) {
    }

    public void newTweetButtonClicked(ActionEvent actionEvent) {
    }

    public void yourTweetsButtonClicked(ActionEvent actionEvent) {
    }

    public void factionsButtonClicked(ActionEvent actionEvent) {
    }

    public void notificationButtonClicked(ActionEvent actionEvent) {
    }


}
