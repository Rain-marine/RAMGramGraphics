package gui.controllers;

import controllers.SettingController;
import gui.controllers.popups.AlertBox;
import gui.controllers.popups.SimpleConfirmBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainMenuGuiController {
    @FXML
    private Button mainMenuButton;
    @FXML
    private Button backButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button personalPageButton;
    @FXML
    private Button timeLineButton;
    @FXML
    private Button explorerButton;
    @FXML
    private Button messagingButton;
    @FXML
    private Button settingButton;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private final SettingController settingsController = new SettingController();

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        AlertBox.display("Nerd Alert","You \"are\" in main menu idiot");
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        AlertBox.display("log out alert", "To go back to login menu, use log out button");
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        boolean answer = SimpleConfirmBox.display("Log out confirmation", "Are you sure you want to Log out??");
        if (answer) {
            settingsController.logout();
            Toolbar.getInstance().changeScene("FXMLs/Welcome/Login.fxml",actionEvent);

        }
    }

    public void personalPageButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().changeScene("FXMLs/PersonalPage/PersonalPageMenu.fxml",actionEvent);
    }

    public void timeLineButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().timeline(actionEvent);
    }

    public void explorerButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().explorer(actionEvent);
    }

    public void messagingButtonClicked(ActionEvent actionEvent) { //messagesMenuGuiController.initialize();
    }

    public void settingButtonClicked(ActionEvent actionEvent) {
        Toolbar.getInstance().changeScene("FXMLs/Setting/SettingMenu.fxml",actionEvent);
    }
}
