package gui.controllers;

import controllers.Controllers;
import gui.controllers.popups.SimpleConfirmBox;
import gui.controllers.tweets.TweetShowerGuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.LoggedUser;
import util.ConfigLoader;

import java.util.ArrayList;
import java.util.Objects;

public class SceneLoader implements Controllers {

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
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource(address)));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void mainMenu(ActionEvent actionEvent) {
        changeScene(ConfigLoader.loadFXML("mainMenuAdd"),actionEvent);
    }

    public void explorer(ActionEvent actionEvent) {
        changeScene(ConfigLoader.loadFXML("explorerAdd"),actionEvent);
    }

    public void timeline(ActionEvent actionEvent) {
        changeScene(ConfigLoader.loadFXML("timelineAdd"),actionEvent);
    }



    public void logout(ActionEvent actionEvent) {
        boolean answer = SimpleConfirmBox.display("Log out confirmation", "Are you sure you want to Log out??");
        if (answer) {
            SETTING_CONTROLLER.logout();
            changeScene(ConfigLoader.loadFXML("loginFXMLAddress"),actionEvent);
        }
    }

    public void noConfirmLogout(ActionEvent actionEvent) {
        SETTING_CONTROLLER.logout();
        changeScene(ConfigLoader.loadFXML("loginFXMLAddress"),actionEvent);
    }

    public void personalPage(ActionEvent actionEvent) {
        changeScene(ConfigLoader.loadFXML("personalPageAdd"),actionEvent);
    }

    public void yourTweets(ActionEvent actionEvent){
        ArrayList<Long> listOfTweets = TWEET_CONTROLLER.getAllTweets(LoggedUser.getLoggedUser().getId());
        TweetShowerGuiController.setListOfTweets(listOfTweets);
        TweetShowerGuiController.setPreviousMenu(ConfigLoader.getPreviousMenuCode("personalPage"));
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("yourTweets"), actionEvent);
    }

    public void messaging(ActionEvent actionEvent) {
        changeScene(ConfigLoader.loadFXML("messageMenu"),actionEvent);
    }

    public void setting(ActionEvent actionEvent) {
        changeScene(ConfigLoader.loadFXML("setting"), actionEvent);
    }
}
