package gui.controllers.mainmenu;

import gui.controllers.SceneLoader;
import gui.controllers.popups.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import util.ConfigLoader;

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

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        AlertBox.display("Nerd Alert","You \"are\" in main menu idiot");
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        AlertBox.display("log out alert", "To go back to login menu, use log out button");
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void personalPageButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().personalPage(actionEvent);
    }

    public void timeLineButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().timeline(actionEvent);
    }

    public void explorerButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().explorer(actionEvent);
    }

    public void messagingButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().messaging(actionEvent);
    }

    public void settingButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().setting(actionEvent);
    }
}
