package gui.controllers.settings;

import controllers.DateFormat;
import controllers.SettingController;
import controllers.UserController;
import gui.controllers.Controllers;
import gui.controllers.Toolbar;
import gui.controllers.popups.SimpleConfirmBox;
import gui.controllers.popups.NewPasswordBoxSimple;
import gui.controllers.popups.PasswordConfirmBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import models.LoggedUser;
import util.ConfigLoader;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class SettingGuiController implements Initializable, Controllers {

    boolean isPublic;

    @FXML
    private Button privateButton;
    @FXML
    private Button publicButton;
    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        isPublic = SETTING_CONTROLLER.isAccountPublic(LoggedUser.getLoggedUser().getUsername());
        if(isPublic){
            privateButton.setStyle("-fx-background-color: #717171;");
            publicButton.setStyle("-fx-background-color: #D7A4FF;");
        }
        else {
            publicButton.setStyle("-fx-background-color: #717171;");
            privateButton.setStyle("-fx-background-color: #D7A4FF;");
        }

    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().mainMenu(actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().logout(actionEvent);
    }


    public void deActiveButtonClicked(ActionEvent actionEvent) {
        boolean answer = SimpleConfirmBox.display("deactivation","Are you sure you want to deActivate your account?");
        if(answer){
            SETTING_CONTROLLER.deActiveAccount();
            Toolbar.getInstance().noConfirmLogout(actionEvent);
        }
    }

    public void changePassButtonClicked(ActionEvent actionEvent) {
        boolean result = PasswordConfirmBox.display();
        if(result){
            NewPasswordBoxSimple.display();
        }
    }

    public void changeBDButtonClicked(ActionEvent actionEvent) {
        String birthday = datePicker.getValue() == null ? "" : datePicker.getValue().toString() ;
        if(!birthday.equals("")) {
            USER_CONTROLLER.changeBirthday(DateFormat.stringToDate(birthday));
        }
    }

    public void deleteAccountButtonClicked(ActionEvent actionEvent) {
        boolean answer = SimpleConfirmBox.display("delete account", "Are you sure you want to delete your account?");
        if(answer){
            boolean result = PasswordConfirmBox.display();
            if(result){
                SETTING_CONTROLLER.deleteAccount();
                Toolbar.getInstance().noConfirmLogout(actionEvent);
            }
        }
    }

    public void PrivacySettingButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().changeScene("FXMLs/Setting/PrivacySetting.fxml",actionEvent);
    }


    public void publicButtonClicked(ActionEvent actionEvent) {
        if(!isPublic){
            SETTING_CONTROLLER.changeAccountVisibility(true);
            privateButton.setStyle("-fx-background-color: #717171;");
            publicButton.setStyle("-fx-background-color: #D7A4FF;");
            isPublic = true;
        }
    }


    public void privateButtonClicked(ActionEvent actionEvent) {
        if(isPublic){
            SETTING_CONTROLLER.changeAccountVisibility(false);
            publicButton.setStyle("-fx-background-color: #717171;");
            privateButton.setStyle("-fx-background-color: #D7A4FF;");
            isPublic = false;
        }
    }
}
