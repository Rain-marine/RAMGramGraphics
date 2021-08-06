package gui.controllers.settings;

import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import models.LoggedUser;
import util.ConfigLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class PrivacySettingGuiController implements Initializable, Controllers {
    @FXML
    private ChoiceBox lastSeenChoice;
    @FXML
    private ChoiceBox birthdayChoice;
    @FXML
    private ChoiceBox numberChoice;
    @FXML
    private ChoiceBox emailChoice;

    private String lastSeenStatus;
    private String numberStatus;
    private String emailStatus;
    private String birthdayStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList options = FXCollections.observableArrayList();
        options.addAll("NONE", "FOLLOWING", "ALL");
        birthdayChoice.getItems().addAll(options);
        numberChoice.getItems().addAll(options);
        emailChoice.getItems().addAll(options);
        lastSeenChoice.getItems().addAll("nobody", "everybody", "following");
        reload();
    }

    private void reload() {
        numberStatus = SETTING_CONTROLLER.getUserNumberStatus(LoggedUser.getLoggedUser()).toString();
        emailStatus = SETTING_CONTROLLER.getUserEmailStatus(LoggedUser.getLoggedUser()).toString();
        birthdayStatus = SETTING_CONTROLLER.getUserBirthdayStatus(LoggedUser.getLoggedUser()).toString();
        lastSeenStatus = SETTING_CONTROLLER.getUserLastSeenStatus(LoggedUser.getLoggedUser().getUsername());
        lastSeenChoice.setValue(lastSeenStatus);
        numberChoice.setValue(numberStatus);
        emailChoice.setValue(emailStatus);
        birthdayChoice.setValue(birthdayStatus);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("setting"), actionEvent);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void saveButtonClicked(ActionEvent actionEvent) {
        boolean hasAnythingChanged = false;
        String newNumStatus = numberChoice.getValue().toString();
        String newBDStatus = birthdayChoice.getValue().toString();
        String newLSStatus = lastSeenChoice.getValue().toString();
        String newEmailStatus = emailChoice.getValue().toString();
        if (!newBDStatus.equals(birthdayStatus)) {
            hasAnythingChanged = true;
            SETTING_CONTROLLER.changeBirthdayStatus(newBDStatus);
        }
        if (!newEmailStatus.equals(emailStatus)) {
            hasAnythingChanged = true;
            SETTING_CONTROLLER.changeEmailStatus(newEmailStatus);
        }
        if (!newNumStatus.equals(numberStatus)) {
            hasAnythingChanged = true;
            SETTING_CONTROLLER.changeNumberStatus(newNumStatus);
        }
        if (!newLSStatus.equals(lastSeenStatus)) {
            hasAnythingChanged = true;
            SETTING_CONTROLLER.changeLastSeenStatus(newLSStatus);
        }
        if (hasAnythingChanged)
            reload();
    }

}
