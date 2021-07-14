package gui.controllers;

import controllers.SettingController;
import gui.controllers.popups.SimpleConfirmBox;
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
    }

    public static Toolbar getInstance(){
        if ( toolbar ==  null ){
            toolbar = new Toolbar();
        }
        return toolbar;
    }

    public void changeScene(String address, ActionEvent actionEvent){
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource(address));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void mainMenu(ActionEvent actionEvent) {
        changeScene(ConfigLoader.readProperty("mainMenuAdd"),actionEvent);
    }

    public void logout(ActionEvent actionEvent) {
        boolean answer = SimpleConfirmBox.display("Log out confirmation", "Are you sure you want to Log out??");
        if (answer) {
            settingsController.logout();
            changeScene(ConfigLoader.readProperty("loginFXMLAddress"),actionEvent);
        }
    }

    public void noConfirmLogout(ActionEvent actionEvent) {
        settingsController.logout();
        changeScene(ConfigLoader.readProperty("loginFXMLAddress"),actionEvent);
    }

}
