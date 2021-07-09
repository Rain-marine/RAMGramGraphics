package gui.controllers;

import controllers.SettingController;
import gui.controllers.popups.AlertBox;
import gui.controllers.popups.ConfirmBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.ConfigLoader;

public class Toolbar {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private final SettingController settingsController = new SettingController();
    private static Toolbar toolbar;

    private Toolbar() {
        this.toolbar = toolbar;
    }

    public static Toolbar getInstance(){
        if ( toolbar ==  null ){
            toolbar = new Toolbar();
        }
        return toolbar;
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(ConfigLoader.readProperty("mainMenuAdd")));
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

    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        boolean answer = ConfirmBox.display("Log out confirmation", "Are you sure you want to Log out??");
        if (answer) {
            settingsController.logout();
            try {
                root = FXMLLoader.load(getClass().getClassLoader().getResource(ConfigLoader.readProperty("loginFXMLAddress")));
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
}
