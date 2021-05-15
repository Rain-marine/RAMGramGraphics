package gui.controllers;

import controllers.SettingController;
import gui.controllers.popups.AlertBox;
import gui.controllers.popups.ConfirmBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

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
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("MainMenu.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        AlertBox.display("log out alert", "To go back to login menu, use log out button");
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        boolean answer = ConfirmBox.display("Log out confirmation", "Are you sure you want to Log out??");
        if (answer) {
            settingsController.logout();
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource("Welcome/Login.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }

        }
    }

    public void personalPageButtonClicked(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("PersonalPage/PersonalPage.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void timeLineButtonClicked(ActionEvent actionEvent) {
        //timeLineGuiController.initialize();

    }

    public void explorerButtonClicked(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Welcome/Explorer.fxml"));
            System.out.println(loader.getLocation());
            Parent p = loader.load();

            ExplorerGuiController controller = loader.getController();
            controller.initialize(1234);

            //explorerGuiController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void messagingButtonClicked(ActionEvent actionEvent) { //messagesMenuGuiController.initialize();
    }

    public void settingButtonClicked(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("Setting/SettingMenu.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
