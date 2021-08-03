package gui.controllers;

import controllers.SettingController;
import gui.controllers.popups.SimpleConfirmBox;
import gui.controllers.tweets.TweetShowerGuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.LoggedUser;
import models.Tweet;
import util.ConfigLoader;

import java.util.ArrayList;
import java.util.List;

public class SceneLoader implements Controllers{

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static SceneLoader sceneLoader;

    private SceneLoader() {
    }

    public static SceneLoader getInstance(){
        if ( sceneLoader ==  null ){
            sceneLoader = new SceneLoader();
        }
        return sceneLoader;
    }

    public void changeScene(String address, ActionEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            root =fxmlLoader.load(getClass().getClassLoader().getResource(address));
            fxmlLoader.getController();
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

    public void explorer(ActionEvent actionEvent) {
        changeScene(ConfigLoader.readProperty("explorerAdd"),actionEvent);
    }

    public void timeline(ActionEvent actionEvent) {
        changeScene(ConfigLoader.readProperty("timelineAdd"),actionEvent);
    }



    public void logout(ActionEvent actionEvent) {
        boolean answer = SimpleConfirmBox.display("Log out confirmation", "Are you sure you want to Log out??");
        if (answer) {
            SETTING_CONTROLLER.logout();
            changeScene(ConfigLoader.readProperty("loginFXMLAddress"),actionEvent);
        }
    }

    public void noConfirmLogout(ActionEvent actionEvent) {
        SETTING_CONTROLLER.logout();
        changeScene(ConfigLoader.readProperty("loginFXMLAddress"),actionEvent);
    }

    public void personalPage(ActionEvent actionEvent) {
        changeScene(ConfigLoader.readProperty("personalPageAdd"),actionEvent);
    }

    public void yourTweets(ActionEvent actionEvent){
        ArrayList<Long> listOfTweets = TWEET_CONTROLLER.getAllTweets(LoggedUser.getLoggedUser().getId());
        TweetShowerGuiController.setListOfTweets(listOfTweets);
        TweetShowerGuiController.setPreviousMenu(6);
        SceneLoader.getInstance().changeScene(ConfigLoader.readProperty("yourTweets"), actionEvent);
    }

    public void messaging(ActionEvent actionEvent) {
        changeScene(ConfigLoader.readProperty("messageMenu"),actionEvent);
    }
}
