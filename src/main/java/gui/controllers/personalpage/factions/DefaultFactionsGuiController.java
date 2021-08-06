package gui.controllers.personalpage.factions;

import controllers.ProfileAccessController;
import controllers.Controllers;
import gui.controllers.SceneLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.User;
import util.ConfigLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DefaultFactionsGuiController implements Initializable, Controllers {

    @FXML
    private ScrollPane factionArea;

    public enum LIST {FOLLOWING , BLACKLIST , FOLLOWER}
    private static LIST list;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<User> members = null;
        int factionID = 0;
        switch (list){
            case FOLLOWER -> {
                members = FACTIONS_CONTROLLER.getActiveFollowers();
                factionID = -1;
            }
            case FOLLOWING -> {
                members = FACTIONS_CONTROLLER.getActiveFollowings();
                factionID = -2;
            }
            case BLACKLIST -> {members = FACTIONS_CONTROLLER.getActiveBlockedUsers();
                factionID = -3;
            }
        }
        VBox userList = new VBox(5);
        for (User member : members) {
            HBox userCard = new HBox(5);
            Label name = new Label(member.getUsername());
            Button profile = new Button("profile");
            profile.setId(String.valueOf(member.getId()));
            int finalFactionID = factionID;
            profile.setOnAction(event -> {
                ProfileAccessController profileAccessController = new ProfileAccessController(4, member.getId(), finalFactionID);
                SceneLoader.getInstance().changeScene(profileAccessController.checkAccessibility(), event);
            });
            userCard.getChildren().addAll(name , profile);
            userList.getChildren().add(userCard);
        }
        factionArea.setContent(userList);
    }

    public void logoutButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().logout(actionEvent);
    }

    public void backButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().changeScene(ConfigLoader.loadFXML("factionList"),actionEvent);
    }

    public void mainMenuButtonClicked(ActionEvent actionEvent) {
        SceneLoader.getInstance().mainMenu(actionEvent);
    }

    public static LIST getList() {
        return list;
    }

    public static void setList(LIST list) {
        DefaultFactionsGuiController.list = list;
    }
}
